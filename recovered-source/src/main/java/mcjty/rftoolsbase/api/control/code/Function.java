package mcjty.rftoolsbase.api.control.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import mcjty.rftoolsbase.api.control.parameters.ParameterType;

public class Function {
   private final String id;
   private final String name;
   private final IFunctionRunnable functionRunnable;
   private final List<String> description;
   private final ParameterType returnType;

   private Function(Function.Builder builder) {
      this.id = builder.id;
      this.name = builder.name;
      this.functionRunnable = builder.functionRunnable;
      this.description = new ArrayList<>(builder.description);
      this.returnType = builder.returnType;
   }

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   @Nonnull
   public IFunctionRunnable getFunctionRunnable() {
      return this.functionRunnable;
   }

   @Nonnull
   public List<String> getDescription() {
      return this.description;
   }

   public ParameterType getReturnType() {
      return this.returnType;
   }

   public static Function.Builder builder() {
      return new Function.Builder();
   }

   public static class Builder {
      private static final IFunctionRunnable NOOP = (processor, program) -> null;
      private String id;
      private IFunctionRunnable functionRunnable = NOOP;
      private List<String> description = Collections.emptyList();
      private ParameterType returnType;
      private String name;

      public Function.Builder id(String id) {
         this.id = id;
         return this;
      }

      public Function.Builder name(String name) {
         this.name = name;
         return this;
      }

      public Function.Builder runnable(IFunctionRunnable runnable) {
         this.functionRunnable = runnable;
         return this;
      }

      public Function.Builder description(String... description) {
         this.description = new ArrayList<>();
         Collections.addAll(this.description, description);
         return this;
      }

      public Function.Builder type(ParameterType type) {
         this.returnType = type;
         return this;
      }

      public Function build() {
         return new Function(this);
      }
   }
}
