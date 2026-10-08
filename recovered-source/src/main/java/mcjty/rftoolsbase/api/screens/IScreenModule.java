package mcjty.rftoolsbase.api.screens;

import javax.annotation.Nonnull;
import mcjty.rftoolsbase.api.screens.data.IModuleData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IScreenModule<M extends IScreenModule<?, T>, T extends IModuleData> {
   T getData(IScreenDataHelper var1, Level var2, long var3);

   M validate(Level var1, BlockPos var2, boolean var3);

   int getRfPerTick();

   @Nonnull
   ItemStack mouseClick(ItemStack var1, Level var2, int var3, int var4, boolean var5, Player var6);

   default boolean needsController() {
      return false;
   }
}
