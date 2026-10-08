package mcjty.rftoolsbase.api.various;

import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface ITabletSupport {
   Item getInstalledTablet();

   void openGui(@Nonnull Player var1, @Nonnull ItemStack var2, @Nonnull ItemStack var3);
}
