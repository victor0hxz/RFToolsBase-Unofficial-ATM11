package mcjty.rftoolsbase.api.control.parameters;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.RecordBuilder;
import java.util.Objects;
import mcjty.rftoolsbase.api.control.code.Function;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class Parameter implements IParameter {
   private final ParameterType parameterType;
   private final ParameterValue parameterValue;
   private static final int MODE_FUNCTION = 2;
   private static final int MODE_VARIABLE = 1;
   private static final int MODE_CONSTANT = 0;
   public static final Codec<Parameter> CODEC = new Codec<Parameter>() {
      public <T> DataResult<T> encode(Parameter input, DynamicOps<T> ops, T prefix) {
         RecordBuilder<T> builder = ops.mapBuilder();
         return ParameterType.CODEC
            .encodeStart(ops, input.getParameterType())
            .flatMap(
               typeElement -> {
                  builder.add("type", typeElement);
                  ParameterValue parameterValue = input.getParameterValue();
                  if (parameterValue == null) {
                     return DataResult.error(() -> "Parameter value is missing");
                  } else if (parameterValue.isVariable()) {
                     builder.add("variable", ops.createInt(parameterValue.getVariableIndex()));
                     return builder.build(prefix);
                  } else if (parameterValue.isFunction()) {
                     builder.add("function", ops.createString(parameterValue.getFunction().getId()));
                     return builder.build(prefix);
                  } else {
                     Object constant = parameterValue.getValue();
                     return constant == null
                        ? builder.build(prefix)
                        : ParameterSerializerHelpers.encodeConstant(ops, input.getParameterType(), constant).flatMap(valueElement -> {
                           builder.add("value", valueElement);
                           return builder.build(prefix);
                        });
                  }
               }
            );
      }

      public <T> DataResult<Pair<Parameter, T>> decode(DynamicOps<T> ops, T input) {
         return ops.getMap(input)
            .setLifecycle(Lifecycle.stable())
            .flatMap(
               mapLike -> {
                  T typeElement = (T)mapLike.get("type");
                  return typeElement == null
                     ? DataResult.error(() -> "Missing parameter type")
                     : ParameterType.CODEC
                        .parse(ops, typeElement)
                        .flatMap(
                           type -> {
                              T variableElement = (T)mapLike.get("variable");
                              if (variableElement != null) {
                                 return ops.getNumberValue(variableElement)
                                    .map(Number::intValue)
                                    .map(index -> Pair.of(new Parameter(type, ParameterValue.variable(index)), ops.empty()))
                                    .mapError(error -> "Invalid variable index: " + error);
                              } else {
                                 T functionElement = (T)mapLike.get("function");
                                 if (functionElement != null) {
                                    return ops.getStringValue(functionElement)
                                       .flatMap(
                                          id -> {
                                             Function function = ParameterSerializerHelpers.resolveFunction(id);
                                             return function == null
                                                ? DataResult.error(() -> "Unknown parameter function: " + id)
                                                : DataResult.success(Pair.of(new Parameter(type, ParameterValue.function(function)), ops.empty()));
                                          }
                                       )
                                       .mapError(error -> "Invalid function id: " + error);
                                 } else {
                                    T valueElement = (T)mapLike.get("value");
                                    return valueElement == null
                                       ? DataResult.success(Pair.of(new Parameter(type, ParameterValue.constant(null)), ops.empty()))
                                       : ParameterSerializerHelpers.decodeConstant(ops, type, valueElement)
                                          .flatMap(constant -> DataResult.success(Pair.of(new Parameter(type, ParameterValue.constant(constant)), ops.empty())));
                                 }
                              }
                           }
                        );
               }
            );
      }
   };
   public static final StreamCodec<RegistryFriendlyByteBuf, Parameter> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, Parameter>() {
      public Parameter decode(RegistryFriendlyByteBuf buf) {
         ParameterType type = (ParameterType)ParameterType.STREAM_CODEC.decode(buf);
         int mode = buf.readByte();

         return switch (mode) {
            case 0 -> {
               boolean hasValue = buf.readBoolean();
               Object value = hasValue ? ParameterSerializerHelpers.requireSerializer(type).decodeFromNetwork(buf) : null;
               yield new Parameter(type, ParameterValue.constant(value));
            }
            case 1 -> new Parameter(type, ParameterValue.variable(buf.readVarInt()));
            case 2 -> {
               String id = buf.readUtf();
               Function function = ParameterSerializerHelpers.resolveFunction(id);
               if (function == null) {
                  throw new IllegalStateException("Unknown parameter function: " + id);
               }

               yield new Parameter(type, ParameterValue.function(function));
            }
            default -> throw new IllegalStateException("Unsupported parameter value mode: " + mode);
         };
      }

      public void encode(RegistryFriendlyByteBuf buf, Parameter input) {
         ParameterType.STREAM_CODEC.encode(buf, input.getParameterType());
         ParameterValue parameterValue = input.getParameterValue();
         if (parameterValue.isVariable()) {
            buf.writeByte(1);
            buf.writeVarInt(parameterValue.getVariableIndex());
         } else if (parameterValue.isFunction()) {
            buf.writeByte(2);
            buf.writeUtf(parameterValue.getFunction().getId());
         } else {
            buf.writeByte(0);
            Object constant = parameterValue.getValue();
            if (constant == null) {
               buf.writeBoolean(false);
            } else {
               buf.writeBoolean(true);
               ParameterType type = input.getParameterType();
               ParameterSerializerHelpers.requireSerializer(type).encodeToNetwork(buf, constant);
            }
         }
      }
   };

   private Parameter(ParameterType parameterType, ParameterValue parameterValue) {
      this.parameterType = parameterType;
      this.parameterValue = parameterValue;
   }

   private Parameter(Parameter.Builder builder) {
      this.parameterType = builder.parameterType;
      this.parameterValue = builder.parameterValue;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         IParameter parameter = (IParameter)o;
         return this.parameterType == parameter.getParameterType() && Objects.equals(this.parameterValue, parameter.getParameterValue());
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.parameterType, this.parameterValue);
   }

   @Override
   public boolean isSet() {
      return this.parameterValue != null && this.parameterValue.getValue() != null;
   }

   @Override
   public ParameterType getParameterType() {
      return this.parameterType;
   }

   @Override
   public ParameterValue getParameterValue() {
      return this.parameterValue;
   }

   public static Parameter.Builder builder() {
      return new Parameter.Builder();
   }

   public static class Builder {
      private ParameterType parameterType;
      private ParameterValue parameterValue;

      public Parameter.Builder type(ParameterType parameterType) {
         this.parameterType = parameterType;
         return this;
      }

      public Parameter.Builder value(ParameterValue value) {
         this.parameterValue = value;
         return this;
      }

      public Parameter build() {
         return new Parameter(this);
      }
   }
}
