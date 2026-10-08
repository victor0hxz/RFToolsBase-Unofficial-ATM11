package mcjty.rftoolsbase.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import java.util.Map;
import mcjty.lib.client.RenderHelper;
import mcjty.rftoolsbase.api.screens.ITextRenderHelper;
import mcjty.rftoolsbase.api.screens.ModuleRenderInfo;
import mcjty.rftoolsbase.api.screens.TextAlign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.Identifier;

public class ScreenTextHelper implements ITextRenderHelper {
   private boolean large = false;
   private TextAlign align = TextAlign.ALIGN_LEFT;
   private boolean dirty = true;
   private int textx;
   private String text;
   private boolean truetype = false;
   private Identifier fontId;
   private static final Map<Identifier, Font> trueTypeRenderer = new HashMap<>();

   public int getTextx() {
      return this.textx;
   }

   @Override
   public String getText() {
      return this.text;
   }

   @Override
   public void setDirty() {
      this.dirty = true;
   }

   @Override
   public boolean isLarge() {
      return this.large;
   }

   @Override
   public ITextRenderHelper large(boolean large) {
      this.dirty = true;
      this.large = large;
      return this;
   }

   @Override
   public TextAlign getAlign() {
      return this.align;
   }

   @Override
   public ITextRenderHelper align(TextAlign align) {
      this.dirty = true;
      this.align = align;
      return this;
   }

   @Override
   public void setup(String line, int width, ModuleRenderInfo renderInfo) {
      if (this.dirty || this.truetype != renderInfo.truetype) {
         this.dirty = false;
         this.truetype = renderInfo.truetype;
         this.fontId = renderInfo.fontId;
         Font renderer = getFontRenderer(this.truetype, this.fontId);
         this.textx = this.large ? 4 : 7;
         if (this.truetype) {
            width *= 2;
         }

         this.text = renderer.plainSubstrByWidth(line, (this.large ? width / 8 : width / 4) - this.textx);
         int w = this.large ? (int)(width / 8.8F) : (int)(width / 4.45F);
         switch (this.align) {
            case ALIGN_LEFT:
            default:
               break;
            case ALIGN_CENTER:
               this.textx = this.textx + (w - renderer.width(this.text)) / 2;
               break;
            case ALIGN_RIGHT:
               this.textx = this.textx + (w - renderer.width(this.text));
         }
      }
   }

   @Override
   public void renderText(GuiGraphicsExtractor graphics, MultiBufferSource buffer, int x, int y, int color, ModuleRenderInfo renderInfo) {
      renderScaled(this.fontId, graphics, buffer, this.text, this.textx + x, y, color, this.truetype, renderInfo.getLightmapValue());
   }

   public static void renderScaled(
      Identifier fontId, GuiGraphicsExtractor graphics, MultiBufferSource buffer, String text, int x, int y, int color, boolean truetype, int lightmapValue
   ) {
      Font renderer = getFontRenderer(truetype, fontId);
      graphics.text(renderer, text, x, y, 0xFF000000 | color, false);
   }

   public static void renderScaledTrimmed(
      Identifier fontId,
      PoseStack matrixStack,
      MultiBufferSource buffer,
      String text,
      int x,
      int y,
      int maxwidth,
      int color,
      boolean truetype,
      int lightmapValue
   ) {
      Font renderer = getFontRenderer(truetype, fontId);
      if (truetype) {
         matrixStack.pushPose();
         matrixStack.scale(0.5F, 0.5F, 0.5F);
         text = renderer.plainSubstrByWidth(text, maxwidth * 2);
         RenderHelper.renderText(renderer, text, x * 2, y * 2, color, matrixStack, buffer, lightmapValue);
         matrixStack.popPose();
      } else {
         text = renderer.plainSubstrByWidth(text, maxwidth);
         RenderHelper.renderText(renderer, text, x * 2, y * 2, color, matrixStack, buffer, lightmapValue);
      }
   }

   private static Font getFontRenderer(boolean truetype, Identifier fontId) {
      return Minecraft.getInstance().font;
   }
}
