package mcjty.rftoolsbase.modules.informationscreen.client;

import com.mojang.blaze3d.vertex.PoseStack;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import mcjty.lib.client.HudRenderHelper;
import mcjty.lib.client.HudRenderHelper.HudOrientation;
import mcjty.lib.client.HudRenderHelper.HudPlacement;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.modules.informationscreen.blocks.DefaultPowerInformationScreenInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;

public class DefaultPowerInformationRenderer {
   private static final DecimalFormat format = new DecimalFormat("#.###");

   public static void renderGraphical(PoseStack matrixStack, MultiBufferSource buffer, TypedMap data, Direction orientation, double scale) {
      if (data != null && data.size() != 0) {
         long energy = data.getOptional(DefaultPowerInformationScreenInfo.ENERGY).orElse(0L);
         long maxEnergy = data.getOptional(DefaultPowerInformationScreenInfo.MAXENERGY).orElse(0L);
      }
   }

   public static void renderDefault(PoseStack matrixStack, MultiBufferSource buffer, TypedMap data, Direction orientation, double scale) {
      List<String> log = getLog(data);
      HudPlacement hudPlacement = HudPlacement.HUD_FRONT;
      HudOrientation hudOrientation = HudOrientation.HUD_SOUTH;
      HudRenderHelper.renderHud(
         matrixStack,
         buffer,
         log,
         hudPlacement,
         hudOrientation,
         orientation,
         -orientation.getStepX() * 0.95,
         0.0,
         -orientation.getStepZ() * 0.95,
         (float)(1.0 + scale)
      );
   }

   private static List<String> getLog(TypedMap data) {
      List<String> list = new ArrayList<>();
      list.add("");
      if (data != null && data.size() > 0) {
         long energy = data.getOptional(DefaultPowerInformationScreenInfo.ENERGY).orElse(0L);
         long maxEnergy = data.getOptional(DefaultPowerInformationScreenInfo.MAXENERGY).orElse(0L);
         list.add(ChatFormatting.BLUE + " RF: " + ChatFormatting.WHITE + formatPower(energy));
         list.add(ChatFormatting.BLUE + " Max: " + ChatFormatting.WHITE + formatPower(maxEnergy));
      } else {
         list.add(ChatFormatting.RED + " Not a powercell");
         list.add(ChatFormatting.RED + " or anything that");
         list.add(ChatFormatting.RED + " supports power");
      }

      return list;
   }

   public static String formatPower(long l) {
      if (l < 100000L) {
         return Long.toString(l);
      } else if (l < 10000000L) {
         Double d = l / 1000.0;
         return format.format(d) + "K";
      } else if (l < 10000000000L) {
         Double d = l / 1000000.0;
         return format.format(d) + "M";
      } else {
         Double d = l / 1.0E9;
         return format.format(d) + "G";
      }
   }

   public static float getHudAngle(Direction orientation) {
      float f3 = 0.0F;
      if (orientation != null) {
         f3 = switch (orientation) {
            case NORTH -> 180.0F;
            case WEST -> 90.0F;
            case EAST -> -90.0F;
            default -> 0.0F;
         };
      }

      return f3;
   }

   public static int getPercentageColor(int i) {
      int col;
      if (i < 30) {
         col = -16711936;
      } else if (i < 40) {
         col = -14492416;
      } else if (i < 50) {
         col = -12272896;
      } else if (i < 60) {
         col = -10053376;
      } else if (i < 70) {
         col = -7833856;
      } else if (i < 80) {
         col = -5614336;
      } else if (i < 90) {
         col = -3394816;
      } else {
         col = -1175296;
      }

      return col;
   }
}
