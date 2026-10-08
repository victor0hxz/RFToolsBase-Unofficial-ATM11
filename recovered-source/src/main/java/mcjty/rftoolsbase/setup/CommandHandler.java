package mcjty.rftoolsbase.setup;

import mcjty.lib.McJtyLib;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.rftoolsbase.api.various.IItemCycler;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class CommandHandler {
   public static final String CMD_TESTRECIPE = "testRecipe";
   public static final String CMD_CYCLE_DESTINATION = "cycleDestination";
   public static final Key<Boolean> PARAM_NEXT = new Key("next", Type.BOOLEAN);

   public static void registerCommands() {
      McJtyLib.registerCommand("rftoolsbase", "testRecipe", (player, arguments) -> {
         ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
         if (heldItem.isEmpty()) {
            return false;
         } else {
            if (heldItem.getItem() instanceof CraftingCardItem) {
               CraftingCardItem.testRecipe(player.level(), heldItem);
            }

            return true;
         }
      });
      McJtyLib.registerCommand("rftoolsbase", "cycleDestination", (player, arguments) -> {
         player.getUsedItemHand();
         ItemStack heldItem = player.getItemInHand(player.getUsedItemHand());
         if (heldItem.getItem() instanceof IItemCycler) {
            ((IItemCycler)heldItem.getItem()).cycle(player, heldItem, (Boolean)arguments.get(PARAM_NEXT));
         }

         return true;
      });
   }
}
