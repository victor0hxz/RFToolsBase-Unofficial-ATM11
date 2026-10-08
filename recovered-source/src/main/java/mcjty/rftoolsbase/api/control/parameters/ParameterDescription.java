package mcjty.rftoolsbase.api.control.parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParameterDescription {
   private final String name;
   private final List<String> description;
   private final ParameterType type;
   private final boolean optional;

   private ParameterDescription(ParameterDescription.Builder builder) {
      this.name = builder.name;
      this.type = builder.type;
      this.description = new ArrayList<>(builder.description);
      this.optional = builder.optional;
   }

   public static ParameterDescription.Builder builder() {
      return new ParameterDescription.Builder();
   }

   public String getName() {
      return this.name;
   }

   public boolean isOptional() {
      return this.optional;
   }

   public List<String> getDescription() {
      return this.description;
   }

   public ParameterType getType() {
      return this.type;
   }

   public static class Builder {
      private String name;
      private List<String> description;
      private ParameterType type;
      private boolean optional = false;

      public ParameterDescription.Builder type(ParameterType type) {
         this.type = type;
         return this;
      }

      public ParameterDescription.Builder name(String name) {
         this.name = name;
         return this;
      }

      public ParameterDescription.Builder optional() {
         this.optional = true;
         return this;
      }

      public ParameterDescription.Builder description(String... description) {
         this.description = new ArrayList<>();
         Collections.addAll(this.description, description);
         return this;
      }

      public ParameterDescription build() {
         return new ParameterDescription(this);
      }
   }
}
