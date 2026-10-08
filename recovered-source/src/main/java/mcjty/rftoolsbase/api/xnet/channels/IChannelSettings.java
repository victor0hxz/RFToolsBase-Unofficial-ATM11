package mcjty.rftoolsbase.api.xnet.channels;

import com.google.gson.JsonObject;
import java.util.Map;
import javax.annotation.Nullable;
import mcjty.rftoolsbase.api.xnet.gui.IEditorGui;
import mcjty.rftoolsbase.api.xnet.gui.IndicatorIcon;
import net.minecraft.nbt.CompoundTag;

public interface IChannelSettings {
   IChannelType getType();

   void readFromNBT(CompoundTag var1);

   void writeToNBT(CompoundTag var1);

   default JsonObject writeToJson() {
      return null;
   }

   default void readFromJson(JsonObject data) {
   }

   void tick(int var1, IControllerContext var2);

   void cleanCache();

   int getColors();

   @Nullable
   IndicatorIcon getIndicatorIcon();

   @Nullable
   String getIndicator();

   boolean isEnabled(String var1);

   void createGui(IEditorGui var1);

   void update(Map<String, Object> var1);
}
