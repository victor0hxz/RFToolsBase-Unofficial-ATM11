package mcjty.rftoolsbase.api.xnet.keys;

import mcjty.lib.varia.BlockPosTools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record SidedPos(BlockPos pos, Direction side) implements Comparable<SidedPos> {
   public static final StreamCodec<FriendlyByteBuf, SidedPos> STREAM_CODEC = StreamCodec.composite(
      BlockPos.STREAM_CODEC, SidedPos::pos, Direction.STREAM_CODEC, SidedPos::side, SidedPos::new
   );

   @Override
   public String toString() {
      return "SidedPos{" + BlockPosTools.toString(this.pos) + "/" + this.side.getSerializedName() + "}";
   }

   public int compareTo(SidedPos o) {
      int result = this.pos.compareTo(o.pos);
      if (result == 0) {
         result = this.side.compareTo(o.side);
      }

      return result;
   }
}
