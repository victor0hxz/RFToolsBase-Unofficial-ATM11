package mcjty.rftoolsbase.api.xnet.helper;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import mcjty.lib.varia.OrientationTools;
import mcjty.rftoolsbase.api.xnet.channels.Color;
import mcjty.rftoolsbase.api.xnet.channels.IConnectorSettings;
import mcjty.rftoolsbase.api.xnet.channels.RSMode;
import mcjty.rftoolsbase.api.xnet.gui.IEditorGui;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public abstract class AbstractConnectorSettings implements IConnectorSettings {
   public static final String TAG_RS = "rs";
   public static final String TAG_COLOR = "color";
   public static final String TAG_FACING = "facing";
   public static final AbstractConnectorSettings.BaseSettings DEFAULT_SETTINGS = new AbstractConnectorSettings.BaseSettings(
      RSMode.IGNORED, Color.OFF, Color.OFF, Color.OFF, Color.OFF, null
   );
   protected AbstractConnectorSettings.BaseSettings settings;
   private int colorsMask = 0;
   private int prevPulse = 0;
   protected boolean advanced = false;
   @Nonnull
   private final Direction side;

   public AbstractConnectorSettings(@Nonnull AbstractConnectorSettings.BaseSettings base, @Nonnull Direction side) {
      this.settings = base;
      this.side = side;
      this.calculateColorsMask();
   }

   @Nonnull
   public Direction getSide() {
      return this.side;
   }

   @Nonnull
   public Direction getFacing() {
      return this.settings.facingOverride == null ? this.side : this.settings.facingOverride;
   }

   public RSMode getRsMode() {
      return this.settings.rsMode;
   }

   public int getPrevPulse() {
      return this.prevPulse;
   }

   public void setPrevPulse(int prevPulse) {
      this.prevPulse = prevPulse;
   }

   private void calculateColorsMask() {
      this.colorsMask = 0;
      if (this.settings.color0 != Color.OFF) {
         this.colorsMask = this.colorsMask | 1 << this.settings.color0.ordinal();
      }

      if (this.settings.color1 != Color.OFF) {
         this.colorsMask = this.colorsMask | 1 << this.settings.color1.ordinal();
      }

      if (this.settings.color2 != Color.OFF) {
         this.colorsMask = this.colorsMask | 1 << this.settings.color2.ordinal();
      }

      if (this.settings.color3 != Color.OFF) {
         this.colorsMask = this.colorsMask | 1 << this.settings.color3.ordinal();
      }
   }

   public int getColorsMask() {
      return this.colorsMask;
   }

   @Override
   public void update(Map<String, Object> data) {
      RSMode rsMode;
      if (data.containsKey("rs")) {
         rsMode = RSMode.valueOf(((String)data.get("rs")).toUpperCase());
      } else {
         rsMode = RSMode.IGNORED;
      }

      Color colors0;
      if (data.containsKey("color0")) {
         colors0 = Color.colorByValue((Integer)data.get("color0"));
      } else {
         colors0 = Color.OFF;
      }

      Color colors1;
      if (data.containsKey("color1")) {
         colors1 = Color.colorByValue((Integer)data.get("color1"));
      } else {
         colors1 = Color.OFF;
      }

      Color colors2;
      if (data.containsKey("color2")) {
         colors2 = Color.colorByValue((Integer)data.get("color2"));
      } else {
         colors2 = Color.OFF;
      }

      Color colors3;
      if (data.containsKey("color3")) {
         colors3 = Color.colorByValue((Integer)data.get("color3"));
      } else {
         colors3 = Color.OFF;
      }

      String facing = (String)data.get("facing");
      Direction facingOverride = facing == null ? null : Direction.byName(facing.toLowerCase());
      this.settings = new AbstractConnectorSettings.BaseSettings(rsMode, colors0, colors1, colors2, colors3, facingOverride);
      this.calculateColorsMask();
   }

   protected static <T extends Enum<T>> void setEnumSafe(JsonObject object, String tag, T value) {
      if (value != null) {
         object.add(tag, new JsonPrimitive(value.name()));
      }
   }

   protected static <T extends Enum<T>> T getEnumSafe(JsonObject object, String tag, Function<String, T> translator) {
      return object.has(tag) ? translator.apply(object.get(tag).getAsString()) : null;
   }

   protected static void setIntegerSafe(JsonObject object, String tag, Integer value) {
      if (value != null) {
         object.add(tag, new JsonPrimitive(value));
      }
   }

   protected static Integer getIntegerSafe(JsonObject object, String tag) {
      return object.has(tag) ? object.get(tag).getAsInt() : null;
   }

   protected static int getIntegerNotNull(JsonObject object, String tag) {
      return object.has(tag) ? object.get(tag).getAsInt() : 0;
   }

   protected static boolean getBoolSafe(JsonObject object, String tag) {
      return object.has(tag) ? object.get(tag).getAsBoolean() : false;
   }

   protected void writeToJsonInternal(JsonObject object) {
      setEnumSafe(object, "rsmode", this.settings.rsMode);
      setEnumSafe(object, "color0", this.settings.color0);
      setEnumSafe(object, "color1", this.settings.color1);
      setEnumSafe(object, "color2", this.settings.color2);
      setEnumSafe(object, "color3", this.settings.color3);
      setEnumSafe(object, "side", this.side);
      setEnumSafe(object, "facingoverride", this.settings.facingOverride);
      object.add("advancedneeded", new JsonPrimitive(false));
   }

   protected void readFromJsonInternal(JsonObject object) {
      RSMode rsMode = getEnumSafe(object, "rsmode", BaseStringTranslators::getRSMode);
      Color color0 = getEnumSafe(object, "color0", BaseStringTranslators::getColor);
      Color color1 = getEnumSafe(object, "color1", BaseStringTranslators::getColor);
      Color color2 = getEnumSafe(object, "color2", BaseStringTranslators::getColor);
      Color color3 = getEnumSafe(object, "color3", BaseStringTranslators::getColor);
      Direction facingOverride = getEnumSafe(object, "facingoverride", s -> Direction.byName(s.toLowerCase()));
      this.settings = new AbstractConnectorSettings.BaseSettings(rsMode, color0, color1, color2, color3, facingOverride);
   }

   @Override
   public void readFromNBT(CompoundTag tag) {
      this.prevPulse = tag.getIntOr("prevPulse", 0);
   }

   @Override
   public void writeToNBT(CompoundTag tag) {
      tag.putInt("prevPulse", this.prevPulse);
   }

   protected IEditorGui sideGui(IEditorGui gui) {
      return gui.choices(
         "facing",
         "Side from which to operate",
         this.settings.facingOverride == null ? this.side : this.settings.facingOverride,
         OrientationTools.DIRECTION_VALUES
      );
   }

   protected IEditorGui colorsGui(IEditorGui gui) {
      return gui.colors("color0", "Enable on color", this.settings.color0.getColor(), Color.COLORS)
         .colors("color1", "Enable on color", this.settings.color1.getColor(), Color.COLORS)
         .colors("color2", "Enable on color", this.settings.color2.getColor(), Color.COLORS)
         .colors("color3", "Enable on color", this.settings.color3.getColor(), Color.COLORS);
   }

   protected IEditorGui redstoneGui(IEditorGui gui) {
      return gui.redstoneMode("rs", this.settings.rsMode);
   }

   public record BaseSettings(RSMode rsMode, Color color0, Color color1, Color color2, Color color3, Direction facingOverride) {
      public static final Codec<AbstractConnectorSettings.BaseSettings> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               RSMode.CODEC.fieldOf("rsMode").forGetter(AbstractConnectorSettings.BaseSettings::rsMode),
               Color.CODEC.fieldOf("color0").forGetter(AbstractConnectorSettings.BaseSettings::color0),
               Color.CODEC.fieldOf("color1").forGetter(AbstractConnectorSettings.BaseSettings::color1),
               Color.CODEC.fieldOf("color2").forGetter(AbstractConnectorSettings.BaseSettings::color2),
               Color.CODEC.fieldOf("color3").forGetter(AbstractConnectorSettings.BaseSettings::color3),
               Direction.CODEC.optionalFieldOf("facingOverride").forGetter(s -> Optional.ofNullable(s.facingOverride()))
            )
            .apply(
               instance,
               (rsMode, c0, c1, c2, c3, direction) -> new AbstractConnectorSettings.BaseSettings(rsMode, c0, c1, c2, c3, (Direction)direction.orElse(null))
            )
      );
      public static final StreamCodec<FriendlyByteBuf, AbstractConnectorSettings.BaseSettings> STREAM_CODEC = StreamCodec.composite(
         RSMode.STREAM_CODEC,
         AbstractConnectorSettings.BaseSettings::rsMode,
         Color.STREAM_CODEC,
         AbstractConnectorSettings.BaseSettings::color0,
         Color.STREAM_CODEC,
         AbstractConnectorSettings.BaseSettings::color1,
         Color.STREAM_CODEC,
         AbstractConnectorSettings.BaseSettings::color2,
         Color.STREAM_CODEC,
         AbstractConnectorSettings.BaseSettings::color3,
         ByteBufCodecs.optional(Direction.STREAM_CODEC),
         s -> Optional.ofNullable(s.facingOverride()),
         (rsMode, c0, c1, c2, c3, direction) -> new AbstractConnectorSettings.BaseSettings(rsMode, c0, c1, c2, c3, (Direction)direction.orElse(null))
      );
   }
}
