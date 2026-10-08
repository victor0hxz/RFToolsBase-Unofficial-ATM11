package mcjty.rftoolsbase.api.infoscreen;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import mcjty.lib.typed.TypedMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;

public interface IInformationScreenInfo {
   int MODE_POWER = 0;
   int MODE_POWER_GRAPHICAL = 1;

   int[] getSupportedModes();

   void tick();

   @Nonnull
   TypedMap getInfo(int var1);

   void render(int var1, PoseStack var2, MultiBufferSource var3, @Nonnull TypedMap var4, Direction var5, double var6);
}
