package mcjty.rftoolsbase.modules.crafting.items;

import java.util.List;
import java.util.Objects;
import mcjty.lib.container.BaseSlot;
import mcjty.lib.container.ContainerFactory;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.container.SlotFactory;
import mcjty.lib.container.SlotType;
import mcjty.lib.varia.ItemStackList;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class CraftingCardContainer extends GenericContainer {
   public static final int SLOT_INPUT = 0;
   public static final int GRID_WIDTH = 5;
   public static final int GRID_HEIGHT = 4;
   public static final int INPUT_SLOTS = 20;
   public static final int SLOT_OUT = 20;
   private final int cardIndex;
   public static final Lazy<ContainerFactory> CONTAINER_FACTORY = Lazy.of(() -> new ContainerFactory(0).playerSlots(10, 116));

   public CraftingCardContainer(int id, BlockPos pos, Player player) {
      super(CraftingModule.CONTAINER_CRAFTING_CARD.get(), id, (ContainerFactory)CONTAINER_FACTORY.get(), pos, null, player);
      this.cardIndex = player.getInventory().getSelectedSlot();
   }

   public void setupInventories(IItemHandler itemHandler, Inventory inventory) {
      this.addInventory("player", new InvWrapper(inventory));
      this.generateSlots(inventory.player);
   }

   protected Slot createSlot(SlotFactory slotFactory, Player playerEntity, IItemHandler inventory, int index, int x, int y, SlotType slotType) {
      return (Slot)(slotType == SlotType.SLOT_PLAYERHOTBAR && index == this.cardIndex
         ? new BaseSlot((IItemHandler)this.inventories.get(slotFactory.inventoryName()), null, slotFactory.index(), slotFactory.x(), slotFactory.y()) {
            {
               Objects.requireNonNull(CraftingCardContainer.this);
            }

            public boolean mayPickup(Player player) {
               return false;
            }
         }
         : super.createSlot(slotFactory, playerEntity, inventory, index, x, y, slotType));
   }

   public void setGridContents(Player player, List<ItemStack> stacks) {
      ItemStack craftingCard = player.getItemInHand(InteractionHand.MAIN_HAND);
      ItemStackList s = ItemStackList.create(21);
      int x = 0;
      int y = 0;

      for (int i = 0; i < stacks.size(); i++) {
         if (i == 0) {
            s.set(20, stacks.get(i));
         } else {
            int slot = y * 5 + x;
            s.set(slot, stacks.get(i));
            if (++x >= 3) {
               x = 0;
               y++;
            }
         }
      }

      CraftingCardItem.putStacksInItem(craftingCard, s);
   }
}
