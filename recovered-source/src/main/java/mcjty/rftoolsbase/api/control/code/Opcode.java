package mcjty.rftoolsbase.api.control.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mcjty.rftoolsbase.api.control.parameters.ParameterDescription;

public class Opcode {
   private final String id;
   private final OpcodeOutput opcodeOutput;
   private final boolean isEvent;
   private final List<ParameterDescription> parameters;
   private final String outputDescription;
   private final IOpcodeRunnable runnable;
   private final List<String> description;
   private final Set<OpcodeCategory> categories;
   private final boolean deprecated;
   private final int iconU;
   private final int iconV;
   private final String iconResource;

   private Opcode(Opcode.Builder builder) {
      this.id = builder.id;
      this.opcodeOutput = builder.opcodeOutput;
      this.isEvent = builder.isEvent;
      this.parameters = new ArrayList<>(builder.parameters);
      this.iconU = builder.iconU;
      this.iconV = builder.iconV;
      this.runnable = builder.runnable;
      this.description = new ArrayList<>(builder.description);
      this.categories = EnumSet.copyOf(builder.categories);
      this.outputDescription = builder.outputDescription;
      this.deprecated = builder.deprecated;
      this.iconResource = builder.iconResource;
   }

   public String getId() {
      return this.id;
   }

   public OpcodeOutput getOpcodeOutput() {
      return this.opcodeOutput;
   }

   public boolean isEvent() {
      return this.isEvent;
   }

   public Set<OpcodeCategory> getCategories() {
      return this.categories;
   }

   @Nonnull
   public List<ParameterDescription> getParameters() {
      return this.parameters;
   }

   public boolean isDeprecated() {
      return this.deprecated;
   }

   @Nonnull
   public IOpcodeRunnable getRunnable() {
      return this.runnable;
   }

   public List<String> getDescription() {
      return this.description;
   }

   public String getOutputDescription() {
      return this.outputDescription;
   }

   public int getIconU() {
      return this.iconU;
   }

   public int getIconV() {
      return this.iconV;
   }

   @Nullable
   public String getIconResource() {
      return this.iconResource;
   }

   public static Opcode.Builder builder() {
      return new Opcode.Builder();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Opcode opcode = (Opcode)o;
         return this.id.equals(opcode.id);
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      return "Opcode{" + this.id + "}";
   }

   @Override
   public int hashCode() {
      return this.id.hashCode();
   }

   public static class Builder {
      private static final IOpcodeRunnable NOOP = (processor, program, opcode) -> IOpcodeRunnable.OpcodeResult.POSITIVE;
      private String id;
      private OpcodeOutput opcodeOutput = OpcodeOutput.SINGLE;
      private boolean isEvent = false;
      private int iconU;
      private int iconV;
      private String iconResource;
      private List<ParameterDescription> parameters = new ArrayList<>();
      private IOpcodeRunnable runnable = NOOP;
      private List<String> description = Collections.emptyList();
      private String outputDescription = "No result";
      private Set<OpcodeCategory> categories = EnumSet.noneOf(OpcodeCategory.class);
      private boolean deprecated = false;

      public Opcode.Builder id(String id) {
         this.id = id;
         return this;
      }

      public Opcode.Builder deprecated(boolean deprecated) {
         this.deprecated = deprecated;
         return this;
      }

      public Opcode.Builder description(String... description) {
         this.description = new ArrayList<>();
         Collections.addAll(this.description, description);
         return this;
      }

      public Opcode.Builder outputDescription(String outputDescription) {
         this.outputDescription = outputDescription;
         return this;
      }

      public Opcode.Builder runnable(IOpcodeRunnable runnable) {
         this.runnable = runnable;
         return this;
      }

      public Opcode.Builder opcodeOutput(OpcodeOutput opcodeOutput) {
         this.opcodeOutput = opcodeOutput;
         return this;
      }

      public Opcode.Builder isEvent(boolean isEvent) {
         this.isEvent = isEvent;
         return this;
      }

      public Opcode.Builder icon(int u, int v) {
         this.iconU = u;
         this.iconV = v;
         return this;
      }

      public Opcode.Builder icon(int u, int v, String iconLocation) {
         this.iconU = u;
         this.iconV = v;
         this.iconResource = iconLocation;
         return this;
      }

      public Opcode.Builder category(OpcodeCategory category) {
         this.categories.add(category);
         return this;
      }

      public Opcode.Builder parameter(ParameterDescription parameter) {
         this.parameters.add(parameter);
         return this;
      }

      public Opcode build() {
         return new Opcode(this);
      }
   }
}
