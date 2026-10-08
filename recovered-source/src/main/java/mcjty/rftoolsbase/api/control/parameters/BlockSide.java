package mcjty.rftoolsbase.api.control.parameters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.apache.commons.lang3.StringUtils;

public class BlockSide implements Comparable<BlockSide> {
   @Nullable
   private final String nodeName;
   @Nullable
   private final Direction side;
   private static final int MAX_STRING_LENGTH = 32767;
   public static final Codec<BlockSide> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
            Codec.STRING.optionalFieldOf("node").forGetter(side -> Optional.ofNullable(side.getNodeName())),
            Direction.CODEC.optionalFieldOf("side").forGetter(side -> Optional.ofNullable(side.getSide()))
         )
         .apply(instance, (node, dir) -> new BlockSide((String)node.orElse(null), (Direction)dir.orElse(null)))
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, BlockSide> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
      inv -> Optional.ofNullable(inv.getNodeName()),
      ByteBufCodecs.optional(Direction.STREAM_CODEC),
      inv -> Optional.ofNullable(inv.getSide()),
      (node, side) -> new BlockSide((String)node.orElse(null), (Direction)side.orElse(null))
   );

   public BlockSide(@Nullable String name, @Nullable Direction side) {
      this.nodeName = name != null && !name.isEmpty() ? name : null;
      this.side = side;
   }

   public int compareTo(@Nonnull BlockSide blockSide) {
      if (this.nodeName == null && blockSide.nodeName != null) {
         return -1;
      } else if (this.nodeName != null && blockSide.nodeName == null) {
         return 1;
      } else {
         return this.nodeName == null ? 0 : this.nodeName.compareTo(blockSide.nodeName);
      }
   }

   @Nullable
   public String getNodeName() {
      return this.nodeName;
   }

   @Nonnull
   public String getNodeNameSafe() {
      return this.nodeName == null ? "" : this.nodeName;
   }

   public boolean hasNodeName() {
      return this.nodeName != null && !this.nodeName.isEmpty();
   }

   @Nullable
   public Direction getSide() {
      return this.side;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BlockSide blockSide = (BlockSide)o;
         return (this.nodeName != null ? this.nodeName.equals(blockSide.nodeName) : blockSide.nodeName == null) ? this.side == blockSide.side : false;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.nodeName != null ? this.nodeName.hashCode() : 0;
      return 31 * result + (this.side != null ? this.side.hashCode() : 0);
   }

   @Override
   public String toString() {
      return this.side == null ? "*" : this.side.toString();
   }

   public String getStringRepresentation() {
      Direction facing = this.getSide();
      String s = facing == null ? "" : StringUtils.left(facing.getSerializedName().toUpperCase(), 1);
      return this.getNodeName() == null ? s : StringUtils.left(this.getNodeName(), 7) + " " + s;
   }
}
