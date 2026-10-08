package mcjty.rftoolsbase.api.control.parameters;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.RecordBuilder;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import mcjty.rftoolsbase.api.control.code.Function;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public class ParameterSerializerHelpers {
   private static final EnumMap<ParameterType, ParameterSerializerHelpers.ConstantSerializer> CONSTANT_SERIALIZERS = new EnumMap<>(ParameterType.class);
   private static final String NUMBER_KIND_KEY = "kind";
   private static final String NUMBER_VALUE_KEY = "value";
   private static ParameterSerializerHelpers.FunctionResolver functionResolver = id -> null;

   public static void setFunctionResolver(ParameterSerializerHelpers.FunctionResolver resolver) {
      functionResolver = Objects.requireNonNull(resolver);
   }

   public static void registerSerializer(ParameterType type, ParameterSerializerHelpers.ConstantSerializer serializer) {
      CONSTANT_SERIALIZERS.put(Objects.requireNonNull(type), Objects.requireNonNull(serializer));
   }

   public static Function resolveFunction(String id) {
      return functionResolver == null ? null : functionResolver.resolve(id);
   }

   public static ParameterSerializerHelpers.ConstantSerializer requireSerializer(ParameterType type) {
      ParameterSerializerHelpers.ConstantSerializer serializer = CONSTANT_SERIALIZERS.get(type);
      if (serializer == null) {
         throw new IllegalStateException("No serializer registered for parameter type " + type);
      } else {
         return serializer;
      }
   }

   public static <T> DataResult<T> encodeConstant(DynamicOps<T> ops, ParameterType type, Object value) {
      ParameterSerializerHelpers.ConstantSerializer serializer = CONSTANT_SERIALIZERS.get(type);
      return serializer == null ? DataResult.error(() -> "No serializer registered for parameter type " + type) : serializer.encode(ops, value);
   }

   public static <T> DataResult<Object> decodeConstant(DynamicOps<T> ops, ParameterType type, T value) {
      ParameterSerializerHelpers.ConstantSerializer serializer = CONSTANT_SERIALIZERS.get(type);
      return serializer == null ? DataResult.error(() -> "No serializer registered for parameter type " + type) : serializer.decode(ops, value);
   }

   private static ParameterSerializerHelpers.ConstantSerializer numberSerializer() {
      return new ParameterSerializerHelpers.ConstantSerializer() {
         @Override
         public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
            if (value instanceof Number number) {
               String kind = ParameterSerializerHelpers.numberKind(number);
               Object kindElement = ops.createString(kind);

               T numberElement = (T)(switch (kind) {
                  case "int" -> ops.createInt(number.intValue());
                  case "long" -> ops.createLong(number.longValue());
                  case "float" -> ops.createFloat(number.floatValue());
                  case "double" -> ops.createDouble(number.doubleValue());
                  default -> throw new IllegalStateException("Unknown number kind: " + kind);
               });
               RecordBuilder<T> builder = ops.mapBuilder();
               builder.add("kind", kindElement);
               builder.add("value", numberElement);
               return builder.build(ops.empty());
            } else {
               return DataResult.error(() -> "Expected Number, got " + value);
            }
         }

         @Override
         public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input)
               .setLifecycle(Lifecycle.stable())
               .flatMap(
                  map -> {
                     T kindElement = (T)map.get("kind");
                     if (kindElement == null) {
                        return DataResult.error(() -> "Missing number kind");
                     } else {
                        T valueElement = (T)map.get("value");
                        return valueElement == null
                           ? DataResult.error(() -> "Missing number value")
                           : ops.getStringValue(kindElement).flatMap(kind -> ops.getNumberValue(valueElement).map(number -> {
                              return switch (kind) {
                                 case "int" -> number.intValue();
                                 case "long" -> number.longValue();
                                 case "float" -> number.floatValue();
                                 case "double" -> number.doubleValue();
                                 default -> throw new IllegalStateException("Unknown number kind: " + kind);
                              };
                           }));
                     }
                  }
               );
         }

         @Override
         public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
            Number number = (Number)value;
            String var4 = ParameterSerializerHelpers.numberKind(number);
            switch (var4) {
               case "int":
                  buf.writeByte(0);
                  buf.writeInt(number.intValue());
                  break;
               case "long":
                  buf.writeByte(1);
                  buf.writeLong(number.longValue());
                  break;
               case "float":
                  buf.writeByte(2);
                  buf.writeFloat(number.floatValue());
                  break;
               case "double":
                  buf.writeByte(3);
                  buf.writeDouble(number.doubleValue());
                  break;
               default:
                  throw new IllegalStateException("Unknown number kind");
            }
         }

         @Override
         public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
            return switch (buf.readByte()) {
               case 0 -> buf.readInt();
               case 1 -> buf.readLong();
               case 2 -> buf.readFloat();
               case 3 -> buf.readDouble();
               default -> throw new IllegalStateException("Unknown number kind");
            };
         }
      };
   }

   private static String numberKind(Number number) {
      if (number instanceof Integer || number instanceof Short || number instanceof Byte) {
         return "int";
      } else if (number instanceof Long) {
         return "long";
      } else if (number instanceof Float) {
         return "float";
      } else {
         return number instanceof Double ? "double" : "double";
      }
   }

   static {
      registerSerializer(ParameterType.PAR_STRING, new ParameterSerializerHelpers.ConstantSerializer() {
         @Override
         public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
            return DataResult.success(ops.createString((String)value));
         }

         @Override
         public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).map(s -> s);
         }

         @Override
         public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
            buf.writeUtf((String)value);
         }

         @Override
         public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readUtf();
         }
      });
      registerSerializer(ParameterType.PAR_INTEGER, new ParameterSerializerHelpers.ConstantSerializer() {
         @Override
         public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
            return DataResult.success(ops.createInt((Integer)value));
         }

         @Override
         public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input).map(number -> number.intValue());
         }

         @Override
         public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
            buf.writeInt((Integer)value);
         }

         @Override
         public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readInt();
         }
      });
      registerSerializer(ParameterType.PAR_LONG, new ParameterSerializerHelpers.ConstantSerializer() {
         @Override
         public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
            return DataResult.success(ops.createLong((Long)value));
         }

         @Override
         public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input).map(number -> number.longValue());
         }

         @Override
         public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
            buf.writeLong((Long)value);
         }

         @Override
         public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readLong();
         }
      });
      registerSerializer(ParameterType.PAR_FLOAT, new ParameterSerializerHelpers.ConstantSerializer() {
         @Override
         public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
            return DataResult.success(ops.createFloat((Float)value));
         }

         @Override
         public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input).map(number -> number.floatValue());
         }

         @Override
         public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
            buf.writeFloat((Float)value);
         }

         @Override
         public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readFloat();
         }
      });
      registerSerializer(ParameterType.PAR_BOOLEAN, new ParameterSerializerHelpers.ConstantSerializer() {
         @Override
         public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
            return DataResult.success(ops.createBoolean((Boolean)value));
         }

         @Override
         public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
            return ops.getBooleanValue(input).map(bool -> bool);
         }

         @Override
         public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
            buf.writeBoolean((Boolean)value);
         }

         @Override
         public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readBoolean();
         }
      });
      registerSerializer(ParameterType.PAR_SIDE, new ParameterSerializerHelpers.ConstantSerializer() {
         @Override
         public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
            return BlockSide.CODEC.encodeStart(ops, (BlockSide)value);
         }

         @Override
         public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
            return BlockSide.CODEC.parse(ops, input).map(side -> side);
         }

         @Override
         public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
            BlockSide.STREAM_CODEC.encode(buf, (BlockSide)value);
         }

         @Override
         public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
            return BlockSide.STREAM_CODEC.decode(buf);
         }
      });
      registerSerializer(ParameterType.PAR_INVENTORY, new ParameterSerializerHelpers.ConstantSerializer() {
         @Override
         public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
            return Inventory.CODEC.encodeStart(ops, (Inventory)value);
         }

         @Override
         public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
            return Inventory.CODEC.parse(ops, input).map(inv -> inv);
         }

         @Override
         public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
            Inventory.STREAM_CODEC.encode(buf, (Inventory)value);
         }

         @Override
         public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
            return Inventory.STREAM_CODEC.decode(buf);
         }
      });
      registerSerializer(ParameterType.PAR_ITEM, new ParameterSerializerHelpers.ConstantSerializer() {
         @Override
         public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
            return ItemStack.CODEC.encodeStart(ops, (ItemStack)value);
         }

         @Override
         public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
            return ItemStack.CODEC.parse(ops, input).map(stack -> stack);
         }

         @Override
         public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
            ItemStack.STREAM_CODEC.encode(buf, (ItemStack)value);
         }

         @Override
         public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
            return ItemStack.STREAM_CODEC.decode(buf);
         }
      });
      registerSerializer(ParameterType.PAR_FLUID, new ParameterSerializerHelpers.ConstantSerializer() {
         @Override
         public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
            return FluidStack.CODEC.encodeStart(ops, (FluidStack)value);
         }

         @Override
         public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
            return FluidStack.CODEC.parse(ops, input).map(stack -> stack);
         }

         @Override
         public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
            FluidStack.STREAM_CODEC.encode(buf, (FluidStack)value);
         }

         @Override
         public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
            return FluidStack.STREAM_CODEC.decode(buf);
         }
      });
      registerSerializer(ParameterType.PAR_TUPLE, new ParameterSerializerHelpers.ConstantSerializer() {
         @Override
         public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
            return Tuple.CODEC.encodeStart(ops, (Tuple)value);
         }

         @Override
         public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
            return Tuple.CODEC.parse(ops, input).map(tuple -> tuple);
         }

         @Override
         public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
            Tuple.STREAM_CODEC.encode(buf, (Tuple)value);
         }

         @Override
         public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
            return Tuple.STREAM_CODEC.decode(buf);
         }
      });
      registerSerializer(ParameterType.PAR_VECTOR, new ParameterSerializerHelpers.ConstantSerializer() {
         @Override
         public <T> DataResult<T> encode(DynamicOps<T> ops, Object value) {
            List<Parameter> list = (List<Parameter>)value;
            return Parameter.CODEC.listOf().encodeStart(ops, list);
         }

         @Override
         public <T> DataResult<Object> decode(DynamicOps<T> ops, T input) {
            return Parameter.CODEC.listOf().parse(ops, input).map(list -> List.copyOf(list));
         }

         @Override
         public void encodeToNetwork(RegistryFriendlyByteBuf buf, Object value) {
            List<Parameter> list = (List<Parameter>)value;
            buf.writeInt(list.size());

            for (Parameter parameter : list) {
               Parameter.STREAM_CODEC.encode(buf, parameter);
            }
         }

         @Override
         public Object decodeFromNetwork(RegistryFriendlyByteBuf buf) {
            int size = buf.readInt();
            ArrayList<Parameter> list = new ArrayList<>(size);

            for (int i = 0; i < size; i++) {
               list.add((Parameter)Parameter.STREAM_CODEC.decode(buf));
            }

            return List.copyOf(list);
         }
      });
      registerSerializer(ParameterType.PAR_NUMBER, numberSerializer());
   }

   public interface ConstantSerializer {
      <T> DataResult<T> encode(DynamicOps<T> var1, Object var2);

      <T> DataResult<Object> decode(DynamicOps<T> var1, T var2);

      void encodeToNetwork(RegistryFriendlyByteBuf var1, Object var2);

      Object decodeFromNetwork(RegistryFriendlyByteBuf var1);
   }

   @FunctionalInterface
   public interface FunctionResolver {
      @Nullable
      Function resolve(String var1);
   }
}
