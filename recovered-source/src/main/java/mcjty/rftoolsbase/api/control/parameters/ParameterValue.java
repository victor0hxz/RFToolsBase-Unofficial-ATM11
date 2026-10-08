package mcjty.rftoolsbase.api.control.parameters;

import mcjty.rftoolsbase.api.control.code.Function;

public class ParameterValue {
   private final int variableIndex;
   private final Object value;
   private final Function function;

   private ParameterValue(int variableIndex, Object value, Function function) {
      this.variableIndex = variableIndex;
      this.value = value;
      this.function = function;
   }

   public int getVariableIndex() {
      return this.variableIndex;
   }

   public Object getValue() {
      return this.value;
   }

   public Function getFunction() {
      return this.function;
   }

   public boolean isConstant() {
      return this.variableIndex == -1 && this.function == null;
   }

   public boolean isVariable() {
      return this.variableIndex != -1;
   }

   public boolean isFunction() {
      return this.function != null;
   }

   public static ParameterValue constant(Object value) {
      return new ParameterValue(-1, value, null);
   }

   public static ParameterValue variable(int index) {
      return new ParameterValue(index, null, null);
   }

   public static ParameterValue function(Function function) {
      return new ParameterValue(-1, null, function);
   }
}
