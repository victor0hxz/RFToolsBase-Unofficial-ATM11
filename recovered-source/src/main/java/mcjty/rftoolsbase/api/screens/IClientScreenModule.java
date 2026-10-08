package mcjty.rftoolsbase.api.screens;

import mcjty.rftoolsbase.api.screens.data.IModuleData;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IClientScreenModule<T extends IModuleData> {
   IClientScreenModule.TransformMode getTransformMode(ItemStack var1);

   int getHeight(ItemStack var1);

   void render(GuiGraphicsExtractor var1, MultiBufferSource var2, IModuleRenderHelper var3, Font var4, int var5, T var6, ModuleRenderInfo var7);

   void mouseClick(ItemStack var1, Level var2, int var3, int var4, boolean var5);

   boolean needsServerData();

   public static enum TransformMode {
      NONE,
      TEXT,
      TEXTLARGE,
      ITEM;
   }
}
