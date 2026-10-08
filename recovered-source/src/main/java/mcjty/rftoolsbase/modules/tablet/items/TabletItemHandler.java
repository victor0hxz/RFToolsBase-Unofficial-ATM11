package mcjty.rftoolsbase.modules.tablet.items;

import javax.annotation.Nonnull;
import mcjty.rftoolsbase.api.various.ITabletSupport;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class TabletItemHandler implements IItemHandlerModifiable {
   private final Player player;

   public TabletItemHandler(Player player) {
      this.player = player;
   }

   private ItemStack getTablet() {
      return this.player.getItemInHand(InteractionHand.MAIN_HAND);
   }

   public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
      TabletItem.setContainingItem(this.player, InteractionHand.MAIN_HAND, slot, stack);
   }

   public int getSlots() {
      return 6;
   }

   @Nonnull
   public ItemStack getStackInSlot(int slot) {
      return TabletItem.getContainingItem(this.getTablet(), slot);
   }

   @Nonnull
   public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
      if (!(stack.getItem() instanceof ITabletSupport)) {
         return stack;
      } else if (!this.getStackInSlot(slot).isEmpty()) {
         return stack;
      } else {
         if (!simulate) {
            this.setStackInSlot(slot, stack);
         }

         return ItemStack.EMPTY;
      }
   }

   @Nonnull
   public ItemStack extractItem(int slot, int amount, boolean simulate) {
      if (simulate) {
         return this.getStackInSlot(slot);
      } else {
         ItemStack stack = this.getStackInSlot(slot);
         this.setStackInSlot(slot, ItemStack.EMPTY);
         return stack;
      }
   }

   public int getSlotLimit(int slot) {
      return 1;
   }

   public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
      return stack.getItem() instanceof ITabletSupport;
   }
}
