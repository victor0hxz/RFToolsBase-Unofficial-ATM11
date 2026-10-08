package mcjty.rftoolsbase.api.screens;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class ModuleRenderInfo {
   public final float factor;
   public final BlockPos pos;
   public final int hitx;
   public final int hity;
   public final boolean truetype;
   public final Identifier fontId;
   private final boolean fullbright;
   public final ItemStack moduleStack;

   public ModuleRenderInfo(float factor, BlockPos pos, int hitx, int hity, boolean truetype, boolean fullbright, Identifier fontId, ItemStack moduleStack) {
      this.factor = factor;
      this.pos = pos;
      this.hitx = hitx;
      this.hity = hity;
      this.truetype = truetype;
      this.fullbright = fullbright;
      this.fontId = fontId;
      this.moduleStack = moduleStack;
   }

   public int getLightmapValue() {
      return this.fullbright ? 15728880 : 140;
   }
}
