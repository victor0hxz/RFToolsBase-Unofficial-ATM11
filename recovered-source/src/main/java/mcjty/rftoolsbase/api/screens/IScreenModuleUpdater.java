package mcjty.rftoolsbase.api.screens;

import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IScreenModuleUpdater {
   @Nonnull
   ItemStack update(ItemStack var1, Level var2, Player var3);
}
