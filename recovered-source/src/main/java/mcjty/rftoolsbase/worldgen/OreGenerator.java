package mcjty.rftoolsbase.worldgen;

import java.util.function.Supplier;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class OreGenerator {
   public static final Supplier<PlacementModifierType<?>> FILTER_OVERWORLD = Registration.PLACEMENT_MODIFIERS
      .register("filter_overworld", () -> () -> DimensionBiomeFilter.CODEC_OVERWORLD);
   public static final Supplier<PlacementModifierType<?>> FILTER_DIMENSIONS = Registration.PLACEMENT_MODIFIERS
      .register("filter_dimensions", () -> () -> DimensionBiomeFilter.CODEC_DIMENSION);

   public static void init() {
   }
}
