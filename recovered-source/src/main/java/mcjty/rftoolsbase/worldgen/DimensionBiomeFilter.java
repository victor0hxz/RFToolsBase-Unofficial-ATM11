package mcjty.rftoolsbase.worldgen;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class DimensionBiomeFilter extends PlacementFilter {
   private final boolean isOverworld;
   private static final DimensionBiomeFilter INSTANCE_OVERWORLD = new DimensionBiomeFilter(true);
   private static final DimensionBiomeFilter INSTANCE_DIMENSION = new DimensionBiomeFilter(false);
   public static MapCodec<PlacementModifier> CODEC_OVERWORLD = MapCodec.unit(() -> INSTANCE_OVERWORLD);
   public static MapCodec<PlacementModifier> CODEC_DIMENSION = MapCodec.unit(() -> INSTANCE_DIMENSION);

   public DimensionBiomeFilter(boolean isOverworld) {
      this.isOverworld = isOverworld;
   }

   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
      boolean overworld = context.getLevel().getLevel().dimension() == Level.OVERWORLD;
      if (overworld == this.isOverworld) {
         PlacedFeature placedfeature = (PlacedFeature)context.topFeature()
            .orElseThrow(() -> new IllegalStateException("Tried to biome check an unregistered feature"));
         Holder<Biome> biome = context.getLevel().getBiome(pos);
         return ((Biome)biome.value()).getGenerationSettings().hasFeature(placedfeature);
      } else {
         return false;
      }
   }

   public PlacementModifierType<?> type() {
      return PlacementModifierType.BIOME_FILTER;
   }
}
