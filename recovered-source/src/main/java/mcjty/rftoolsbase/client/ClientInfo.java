package mcjty.rftoolsbase.client;

import net.minecraft.core.BlockPos;

public class ClientInfo {
   private BlockPos selectedTE = null;
   private BlockPos destinationTE = null;
   private BlockPos hilightedBlock = null;
   private long expireHilight = 0L;

   public void hilightBlock(BlockPos c, long expireHilight) {
      this.hilightedBlock = c;
      this.expireHilight = expireHilight;
   }

   public BlockPos getHilightedBlock() {
      return this.hilightedBlock;
   }

   public long getExpireHilight() {
      return this.expireHilight;
   }

   public BlockPos getSelectedTE() {
      return this.selectedTE;
   }

   public void setSelectedTE(BlockPos selectedTE) {
      this.selectedTE = selectedTE;
   }

   public BlockPos getDestinationTE() {
      return this.destinationTE;
   }

   public void setDestinationTE(BlockPos destinationTE) {
      this.destinationTE = destinationTE;
   }
}
