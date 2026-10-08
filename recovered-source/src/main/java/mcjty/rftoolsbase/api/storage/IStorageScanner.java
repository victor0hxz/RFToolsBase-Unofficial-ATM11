package mcjty.rftoolsbase.api.storage;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IStorageScanner {
   ItemStack requestItem(ItemStack var1, int var2, boolean var3);

   void clearCachedCounts();

   ItemStack requestItem(Predicate<ItemStack> var1, boolean var2, int var3, boolean var4);

   int countItems(ItemStack var1, boolean var2);

   int countItems(ItemStack var1, boolean var2, @Nullable Integer var3);

   ItemStack injectStackFromScreen(ItemStack var1, Player var2);

   void giveToPlayerFromScreen(ItemStack var1, boolean var2, Player var3);

   int countItems(Predicate<ItemStack> var1, boolean var2, @Nullable Integer var3);

   @Nonnull
   ItemStack getItem(Predicate<ItemStack> var1, boolean var2);

   ItemStack insertItem(ItemStack var1, boolean var2);

   int insertItem(ItemStack var1);
}
