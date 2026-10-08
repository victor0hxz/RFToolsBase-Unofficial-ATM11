package mcjty.rftoolsbase.api.control.code;

import java.util.HashMap;
import java.util.Map;

public enum OpcodeCategory {
   CATEGORY_CRAFTING("crafting", "Operations related to crafting"),
   CATEGORY_ITEMS("items", "Operations related to handling items"),
   CATEGORY_LIQUIDS("liquids", "Operations related to handling liquids"),
   CATEGORY_ENERGY("energy", "Operations related to handling energy"),
   CATEGORY_REDSTONE("redstone", "Operations related to handling redstone"),
   CATEGORY_GRAPHICS("graphics", "Operations related to graphics"),
   CATEGORY_COMMUNICATION("communication", "Operations related to communication"),
   CATEGORY_NUMBERS("numbers", "Operations and tests on numbers"),
   CATEGORY_VECTORS("vectors", "Operations and tests on vectors");

   private final String name;
   private final String description;
   private static final Map<String, OpcodeCategory> CATEGORY_MAP = new HashMap<>();

   private OpcodeCategory(String name, String description) {
      this.name = name;
      this.description = description;
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public static OpcodeCategory getByName(String name) {
      return CATEGORY_MAP.get(name);
   }

   static {
      for (OpcodeCategory type : values()) {
         CATEGORY_MAP.put(type.getName(), type);
      }
   }
}
