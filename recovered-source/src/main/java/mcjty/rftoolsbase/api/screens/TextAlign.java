package mcjty.rftoolsbase.api.screens;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public enum TextAlign implements StringRepresentable {
   ALIGN_LEFT("Left"),
   ALIGN_CENTER("Center"),
   ALIGN_RIGHT("Right");

   private final String alignment;
   public static final Codec<TextAlign> CODEC = StringRepresentable.fromEnum(TextAlign::values);
   public static final StreamCodec<FriendlyByteBuf, TextAlign> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(TextAlign.class);

   private TextAlign(String alignment) {
      this.alignment = alignment;
   }

   public static TextAlign get(String alignment) {
      if ("Left".equals(alignment)) {
         return ALIGN_LEFT;
      } else {
         return "Right".equals(alignment) ? ALIGN_RIGHT : ALIGN_CENTER;
      }
   }

   public String getSerializedName() {
      return this.alignment;
   }
}
