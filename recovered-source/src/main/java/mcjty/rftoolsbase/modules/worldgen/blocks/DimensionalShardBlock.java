package mcjty.rftoolsbase.modules.worldgen.blocks;

import java.util.Random;
import javax.annotation.Nonnull;
import mcjty.lib.setup.RegistrationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;

public class DimensionalShardBlock extends Block {
   private final Random rand = new Random();

   public DimensionalShardBlock() {
      super(
         RegistrationContext.prepareBlockProperties(Properties.of().sound(SoundType.METAL).mapColor(MapColor.METAL).strength(3.0F, 5.0F).lightLevel(value -> 7))
      );
   }

   public void destroy(LevelAccessor world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
      if (world.isClientSide()) {
         for (int i = 0; i < 10; i++) {
            world.addParticle(
               ParticleTypes.FIREWORK,
               pos.getX() + 0.5F,
               pos.getY() + 0.5F,
               pos.getZ() + 0.5F,
               this.rand.nextGaussian() / 3.0,
               this.rand.nextGaussian() / 3.0,
               this.rand.nextGaussian() / 3.0
            );
         }
      }
   }
}
