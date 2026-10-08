package mcjty.rftoolsbase.api.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import mcjty.rftoolsbase.api.screens.data.IModuleDataContents;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.MultiBufferSource;

public interface IModuleRenderHelper {
   @Deprecated
   void renderLevel(
      GuiGraphicsExtractor var1,
      MultiBufferSource var2,
      Font var3,
      int var4,
      int var5,
      IModuleDataContents var6,
      String var7,
      boolean var8,
      boolean var9,
      boolean var10,
      boolean var11,
      int var12,
      int var13,
      int var14,
      int var15,
      FormatStyle var16
   );

   ITextRenderHelper createTextRenderHelper();

   ILevelRenderHelper createLevelRenderHelper();

   String format(String var1, FormatStyle var2);

   void renderText(GuiGraphicsExtractor var1, MultiBufferSource var2, int var3, int var4, int var5, @Nonnull ModuleRenderInfo var6, String var7);

   void renderTextTrimmed(PoseStack var1, MultiBufferSource var2, int var3, int var4, int var5, @Nonnull ModuleRenderInfo var6, String var7, int var8);
}
