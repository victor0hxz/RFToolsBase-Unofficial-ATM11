package mcjty.rftoolsbase.modules.hud.client;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import mcjty.lib.client.HudRenderHelper;
import mcjty.lib.client.HudRenderHelper.HudOrientation;
import mcjty.lib.client.HudRenderHelper.HudPlacement;
import mcjty.lib.network.Networking;
import mcjty.lib.network.PacketGetListFromServer;
import mcjty.rftoolsbase.api.client.IHudSupport;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;

public class HudRenderer {
   public static void renderHud(PoseStack matrixStack, MultiBufferSource buffer, IHudSupport hudSupport) {
      renderHud(matrixStack, buffer, hudSupport, 0.0F, false);
   }

   public static void renderHud(PoseStack matrixStack, MultiBufferSource buffer, IHudSupport support, float scale, boolean faceVert) {
      List<String> log = support.getClientLog();
      long t = System.currentTimeMillis();
      if (t - support.getLastUpdateTime() > 250L) {
         Networking.sendToServer(PacketGetListFromServer.create(support.getHudPos(), "getHudLog"));
         support.setLastUpdateTime(t);
      }

      Direction orientation = support.getBlockOrientation();
      HudPlacement hudPlacement = support.isBlockAboveAir() ? HudPlacement.HUD_ABOVE : HudPlacement.HUD_ABOVE_FRONT;
      HudOrientation hudOrientation = orientation == null ? HudOrientation.HUD_TOPLAYER_HORIZ : HudOrientation.HUD_SOUTH;
      HudRenderHelper.renderHud(matrixStack, buffer, log, hudPlacement, hudOrientation, orientation, 0.0, 0.0, 0.0, 1.0F + scale);
   }
}
