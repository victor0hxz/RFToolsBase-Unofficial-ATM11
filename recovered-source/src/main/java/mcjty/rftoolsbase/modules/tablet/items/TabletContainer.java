package mcjty.rftoolsbase.modules.tablet.items;

import java.util.Objects;
import mcjty.lib.container.BaseSlot;
import mcjty.lib.container.ContainerFactory;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.container.SlotDefinition;
import mcjty.lib.container.SlotFactory;
import mcjty.lib.container.SlotType;
import mcjty.rftoolsbase.api.various.ITabletSupport;
import mcjty.rftoolsbase.modules.tablet.TabletModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class TabletContainer extends GenericContainer {
   private int cardIndex;
   public static final int NUM_SLOTS = 6;
   public static final Lazy<ContainerFactory> CONTAINER_FACTORY = Lazy.of(
      () -> new ContainerFactory(1).box(SlotDefinition.specific(s -> s.getItem() instanceof ITabletSupport), 0, 15, 13, 6, 23, 1, 18).playerSlots(10, 106)
   );

   public TabletContainer(int id, BlockPos pos, Player player) {
      super(TabletModule.CONTAINER_TABLET.get(), id, (ContainerFactory)CONTAINER_FACTORY.get(), pos, null, player);
      this.cardIndex = player.getInventory().getSelectedSlot();
   }

   public void setupInventories(IItemHandler itemHandler, Inventory inventory) {
      this.addInventory("container", itemHandler);
      this.addInventory("player", new InvWrapper(inventory));
      this.generateSlots(inventory.player);
   }

   protected Slot createSlot(SlotFactory slotFactory, Player playerEntity, IItemHandler inventory, int index, int x, int y, SlotType slotType) {
      return (Slot)(slotType == SlotType.SLOT_PLAYERHOTBAR && index == this.cardIndex
         ? new BaseSlot((IItemHandler)this.inventories.get(slotFactory.inventoryName()), this.be, slotFactory.index(), slotFactory.x(), slotFactory.y()) {
            {
               Objects.requireNonNull(TabletContainer.this);
            }

            public boolean mayPickup(Player player) {
               return false;
            }
         }
         : super.createSlot(slotFactory, playerEntity, inventory, index, x, y, slotType));
   }
}
