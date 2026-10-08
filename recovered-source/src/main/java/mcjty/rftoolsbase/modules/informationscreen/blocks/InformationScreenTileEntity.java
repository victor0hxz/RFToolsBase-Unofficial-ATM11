package mcjty.rftoolsbase.modules.informationscreen.blocks;

import javax.annotation.Nullable;
import mcjty.lib.tileentity.TickingTileEntity;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.EnergyTools;
import mcjty.lib.varia.OrientationTools;
import mcjty.rftoolsbase.api.infoscreen.CapabilityInformationScreenInfo;
import mcjty.rftoolsbase.api.infoscreen.IInformationScreenInfo;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class InformationScreenTileEntity extends TickingTileEntity {
   private int mode = 0;
   private int cnt = 0;
   private long lastHudTime = 0L;
   private TypedMap clientData;

   public InformationScreenTileEntity(BlockPos pos, BlockState state) {
      super(InformationScreenModule.TYPE_INFORMATION_SCREEN.get(), pos, state);
   }

   public Direction getBlockOrientation() {
      BlockState state = this.level.getBlockState(this.worldPosition);
      return state.getBlock() instanceof InformationScreenBlock ? OrientationTools.getOrientationHoriz(state) : null;
   }

   public void tickServer() {
      this.cnt--;
      if (this.cnt <= 0) {
         this.cnt = 10;
         BlockPos offset = this.getBlockPos().relative(this.getBlockOrientation().getOpposite());
         IInformationScreenInfo capability = (IInformationScreenInfo)this.level
            .getCapability(CapabilityInformationScreenInfo.INFORMATION_SCREEN_INFO_CAPABILITY, offset, null);
         if (capability != null) {
            capability.tick();
         }
      }
   }

   public void toggleMode() {
      IInformationScreenInfo h = this.getInfo();
      int[] modes = h.getSupportedModes();
      int found = -1;

      for (int i = 0; i < modes.length; i++) {
         if (modes[i] == this.mode) {
            found = i;
            break;
         }
      }

      if (found != -1) {
         this.mode = modes[++found % modes.length];
         this.markDirtyClient();
      }
   }

   public int getMode() {
      return this.mode;
   }

   protected void saveAdditional(ValueOutput output) {
      super.saveAdditional(output);
      output.putByte("mode", (byte)this.mode);
   }

   protected void loadAdditional(ValueInput input) {
      super.loadAdditional(input);
      this.mode = input.getByteOr("mode", (byte)0);
   }

   public void saveClientDataToNBT(CompoundTag tag, Provider provider) {
      tag.putByte("mode", (byte)this.mode);
   }

   public void loadClientDataFromNBT(CompoundTag tag, Provider provider) {
      this.mode = tag.getByteOr("mode", (byte)0);
   }

   public void setClientData(TypedMap power) {
      this.clientData = power;
   }

   public TypedMap getClientData() {
      return this.clientData;
   }

   public long getLastUpdateTime() {
      return this.lastHudTime;
   }

   public void setLastUpdateTime(long t) {
      this.lastHudTime = t;
   }

   @Nullable
   public IInformationScreenInfo getInfo() {
      BlockPos offset = this.getBlockPos().relative(this.getBlockOrientation().getOpposite());
      BlockEntity te = this.level.getBlockEntity(offset);
      if (te != null) {
         IInformationScreenInfo capability = (IInformationScreenInfo)this.level
            .getCapability(CapabilityInformationScreenInfo.INFORMATION_SCREEN_INFO_CAPABILITY, offset, null);
         if (capability != null) {
            return capability;
         }

         if (EnergyTools.isEnergyTE(te, this.getBlockOrientation())) {
            return new DefaultPowerInformationScreenInfo(te);
         }
      }

      return null;
   }
}
