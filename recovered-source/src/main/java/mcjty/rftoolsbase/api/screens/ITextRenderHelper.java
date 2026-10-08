package mcjty.rftoolsbase.api.screens;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.MultiBufferSource;

public interface ITextRenderHelper {
   void setup(String var1, int var2, ModuleRenderInfo var3);

   String getText();

   void setDirty();

   boolean isLarge();

   ITextRenderHelper large(boolean var1);

   TextAlign getAlign();

   ITextRenderHelper align(TextAlign var1);

   void renderText(GuiGraphicsExtractor var1, MultiBufferSource var2, int var3, int var4, int var5, ModuleRenderInfo var6);
}
