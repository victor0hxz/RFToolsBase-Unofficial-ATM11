package mcjty.rftoolsbase.api.screens;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mcjty.rftoolsbase.api.screens.data.IModuleDataContents;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.MultiBufferSource;

public interface ILevelRenderHelper {
   void render(GuiGraphicsExtractor var1, MultiBufferSource var2, int var3, int var4, @Nullable IModuleDataContents var5, @Nonnull ModuleRenderInfo var6);

   ILevelRenderHelper label(String var1);

   ILevelRenderHelper settings(boolean var1, BarMode var2);

   ILevelRenderHelper color(int var1, int var2);

   ILevelRenderHelper gradient(int var1, int var2);

   ILevelRenderHelper format(FormatStyle var1);

   int getPosColor();

   int getNegColor();

   int getGradient1();

   int getGradient2();

   FormatStyle getFormatStyle();

   BarMode getBarMode();

   boolean isHideBar();

   String getLabel();

   void setPosColor(int var1);

   void setNegColor(int var1);

   void setGradient1(int var1);

   void setGradient2(int var1);

   void setFormatStyle(FormatStyle var1);

   void setBarMode(BarMode var1);

   void setHideBar(boolean var1);

   void setLabel(String var1);
}
