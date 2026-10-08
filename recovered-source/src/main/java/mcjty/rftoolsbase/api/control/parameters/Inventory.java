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

public class Inventory extends BlockSide {
   @Nullable
   private final Direction intSide;
   public static final Codec<Inventory> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
            Codec.STRING.optionalFieldOf("node").forGetter(inv -> Optional.ofNullable(inv.getNodeName())),
            Direction.CODEC.fieldOf("side").forGetter(Inventory::getSide),
            Direction.CODEC.optionalFieldOf("int_side").forGetter(inv -> Optional.ofNullable(inv.getIntSide()))
         )
         .apply(instance, (node, side, intSide) -> new Inventory((String)node.orElse(null), side, (Direction)intSide.orElse(null)))
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, Inventory> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
      inv -> Optional.ofNullable(inv.getNodeName()),
      Direction.STREAM_CODEC,
      Inventory::getSide,
      ByteBufCodecs.optional(Direction.STREAM_CODEC),
      inv -> Optional.ofNullable(inv.intSide),
      (node, side, intSide) -> new Inventory((String)node.orElse(null), side, (Direction)intSide.orElse(null))
   );

   public Inventory(@Nullable String name, @Nonnull Direction side, @Nullable Direction intSide) {
      super(name, side);
      this.intSide = intSide;
   }

   public String serialize() {
      return "#"
         + (this.hasNodeName() ? this.getNodeName() : "-")
         + "#"
         + this.getSide().getSerializedName()
         + "#"
         + (this.intSide == null ? "-" : this.intSide.getSerializedName())
         + "#";
   }

   public static Inventory deserialize(String s) {
      String[] splitted = StringUtils.split(s, '#');
      return new Inventory(
         "-".equals(splitted[0]) ? null : splitted[0], Direction.byName(splitted[1]), "-".equals(splitted[2]) ? null : Direction.byName(splitted[2])
      );
   }

   @Nonnull
   @Override
   public Direction getSide() {
      return super.getSide();
   }

   @Nullable
   public Direction getIntSide() {
      return this.intSide;
   }

   @Override
   public String getStringRepresentation() {
      String s = StringUtils.left(this.getSide().getSerializedName().toUpperCase(), 1);
      if (this.getIntSide() == null) {
         s = s + "/*";
      } else {
         String is = StringUtils.left(this.getIntSide().getSerializedName().toUpperCase(), 1);
         s = s + "/" + is;
      }

      return this.getNodeName() == null ? s : StringUtils.left(this.getNodeName(), 6) + " " + s;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o == null || this.getClass() != o.getClass()) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else {
         Inventory inventory = (Inventory)o;
         return this.intSide == inventory.intSide;
      }
   }

   @Override
   public int hashCode() {
      int result = super.hashCode();
      return 31 * result + (this.intSide != null ? this.intSide.hashCode() : 0);
   }
}
