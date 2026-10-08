package mcjty.rftoolsbase.modules.filter.items;

import java.util.Objects;
import mcjty.lib.container.BaseSlot;
import mcjty.lib.container.ContainerFactory;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.container.SlotFactory;
import mcjty.lib.container.SlotType;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class FilterModuleContainer extends GenericContainer {
   private final int cardIndex;
   public static final Lazy<ContainerFactory> CONTAINER_FACTORY = Lazy.of(() -> new ContainerFactory(0).playerSlots(60, 106));

   public FilterModuleContainer(int id, BlockPos pos, Player player) {
      super(FilterModule.CONTAINER_FILTER_MODULE.get(), id, (ContainerFactory)CONTAINER_FACTORY.get(), pos, null, player);
      this.cardIndex = player.getInventory().getSelectedSlot();
   }

   public void setupInventories(IItemHandler itemHandler, Inventory inventory) {
      this.addInventory("player", new InvWrapper(inventory));
      this.generateSlots(inventory.player);
   }

   protected Slot createSlot(SlotFactory slotFactory, Player playerEntity, IItemHandler inventory, int index, int x, int y, SlotType slotType) {
      return (Slot)(slotType == SlotType.SLOT_PLAYERHOTBAR && index == this.cardIndex
         ? new BaseSlot((IItemHandler)this.inventories.get(slotFactory.inventoryName()), this.be, slotFactory.index(), slotFactory.x(), slotFactory.y()) {
            {
               Objects.requireNonNull(FilterModuleContainer.this);
            }

            public boolean mayPickup(Player player) {
               return false;
            }
         }
         : super.createSlot(slotFactory, playerEntity, inventory, index, x, y, slotType));
   }
}
