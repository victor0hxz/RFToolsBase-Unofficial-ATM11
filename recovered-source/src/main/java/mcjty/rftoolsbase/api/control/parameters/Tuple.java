package mcjty.rftoolsbase.api.control.parameters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Tuple(int x, int y) implements Comparable<Tuple> {
   public static final Codec<Tuple> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(Codec.INT.fieldOf("x").forGetter(Tuple::x), Codec.INT.fieldOf("y").forGetter(Tuple::y)).apply(instance, Tuple::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, Tuple> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INT, Tuple::x, ByteBufCodecs.INT, Tuple::y, Tuple::new
   );

   public int compareTo(Tuple tuple) {
      if (this.x < tuple.x) {
         return -1;
      } else if (this.x > tuple.x) {
         return 1;
      } else if (this.y < tuple.y) {
         return -1;
      } else {
         return this.y > tuple.y ? 1 : 0;
      }
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   @Override
   public String toString() {
      return this.x + "," + this.y;
   }
}
