package mcjty.rftoolsbase.modules.informationscreen.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.LegacyCapabilities;
import mcjty.rftoolsbase.api.infoscreen.IInformationScreenInfo;
import mcjty.rftoolsbase.modules.informationscreen.client.DefaultPowerInformationRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class DefaultPowerInformationScreenInfo implements IInformationScreenInfo {
   private final BlockEntity tileEntity;
   public static final Key<Long> ENERGY = new Key("energy", Type.LONG);
   public static final Key<Long> MAXENERGY = new Key("maxenergy", Type.LONG);

   public DefaultPowerInformationScreenInfo(BlockEntity tileEntity) {
      this.tileEntity = tileEntity;
   }

   @Override
   public int[] getSupportedModes() {
      return new int[]{0, 1};
   }

   @Override
   public void tick() {
   }

   @Nonnull
   @Override
   public TypedMap getInfo(int mode) {
      IEnergyStorage h = (IEnergyStorage)this.tileEntity.getLevel().getCapability(LegacyCapabilities.ENERGY_BLOCK, this.tileEntity.getBlockPos(), null);
      return h != null ? TypedMap.builder().put(ENERGY, (long)h.getEnergyStored()).put(MAXENERGY, (long)h.getMaxEnergyStored()).build() : TypedMap.EMPTY;
   }

   @Override
   public void render(int mode, PoseStack matrixStack, MultiBufferSource buffer, @Nonnull TypedMap data, Direction orientation, double scale) {
      if (mode == 0) {
         DefaultPowerInformationRenderer.renderDefault(matrixStack, buffer, data, orientation, scale);
      } else {
         DefaultPowerInformationRenderer.renderGraphical(matrixStack, buffer, data, orientation, scale);
      }
   }
}
