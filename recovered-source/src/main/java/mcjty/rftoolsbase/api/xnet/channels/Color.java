package mcjty.rftoolsbase.api.xnet.channels;

import com.mojang.serialization.Codec;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public enum Color implements StringRepresentable {
   OFF(0),
   WHITE(16777215),
   RED(16711680),
   GREEN(65280),
   BLUE(255),
   YELLOW(16776960),
   CYAN(65535),
   PURPLE(16711935),
   ORANGE(16746496),
   GRAY(8947848),
   DARK_RED(8912896),
   DARK_GREEN(34816),
   DARK_BLUE(136),
   DARK_YELLOW(8947712),
   DARK_CYAN(34952),
   DARK_PURPLE(8913032);

   private final int color;
   private static final Map<Integer, Color> COLOR_MAP = new HashMap<>();
   public static final Integer[] COLORS = new Integer[values().length];
   public static final Codec<Color> CODEC = StringRepresentable.fromEnum(Color::values);
   public static final StreamCodec<FriendlyByteBuf, Color> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Color.class);

   private Color(int color) {
      this.color = color;
   }

   public int getColor() {
      return this.color;
   }

   public static Color colorByValue(int color) {
      return COLOR_MAP.get(color);
   }

   public String getSerializedName() {
      return this.name();
   }

   static {
      for (int i = 0; i < values().length; i++) {
         Color col = values()[i];
         COLORS[i] = col.color;
         COLOR_MAP.put(col.color, col);
      }
   }
}
