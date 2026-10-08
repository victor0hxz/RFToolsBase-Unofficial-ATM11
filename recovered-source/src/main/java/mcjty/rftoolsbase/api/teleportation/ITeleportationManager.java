package mcjty.rftoolsbase.api.teleportation;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ITeleportationManager {
   String getReceiverName(Level var1, BlockPos var2);

   boolean createReceiver(Level var1, BlockPos var2, String var3, int var4);

   String getReceiverName(ItemStack var1);

   void teleportPlayer(Player var1, ResourceKey<Level> var2, BlockPos var3);

   void removeReceiverDestinations(Level var1, ResourceKey<Level> var2);
}
