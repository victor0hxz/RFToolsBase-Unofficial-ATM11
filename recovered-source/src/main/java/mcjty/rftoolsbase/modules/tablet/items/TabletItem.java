package mcjty.rftoolsbase.modules.tablet.items;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import mcjty.lib.builder.InfoLine;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.gui.ManualEntry;
import mcjty.lib.items.BaseItem;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.ComponentFactory;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.api.various.IItemCycler;
import mcjty.rftoolsbase.api.various.ITabletSupport;
import mcjty.rftoolsbase.modules.tablet.TabletModule;
import mcjty.rftoolsbase.modules.tablet.data.TabletData;
import mcjty.rftoolsbase.tools.ManualHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;

public class TabletItem extends BaseItem implements IItemCycler, ITooltipSettings {
   public static final ManualEntry MANUAL = ManualHelper.create("rftoolsbase:tools/tablet");
   private final Lazy<TooltipBuilder> tooltipBuilder = Lazy.of(
      () -> new TooltipBuilder()
         .info(new InfoLine[]{TooltipBuilder.key("message.rftoolsbase.shiftmessage")})
         .infoShift(new InfoLine[]{TooltipBuilder.header(), TooltipBuilder.gold()})
   );

   public ManualEntry getManualEntry() {
      return MANUAL;
   }

   public TabletItem() {
      super(RFToolsBase.setup.defaultProperties().stacksTo(1));
   }

   public static int getCurrentSlot(ItemStack stack) {
      TabletData data = (TabletData)stack.getOrDefault(TabletModule.ITEM_TABLET_DATA, TabletData.EMPTY);
      return data.current();
   }

   public static void setCurrentSlot(Player player, ItemStack stack, int current) {
      TabletData data = (TabletData)stack.getOrDefault(TabletModule.ITEM_TABLET_DATA, TabletData.EMPTY);
      data = data.withCurrent(current);
      ItemStack containingItem = getContainingItem(stack, current);
      ItemStack newTablet = deriveNewItemstack(current, containingItem, data, current);
      player.getInventory().getNonEquipmentItems().set(player.getInventory().getSelectedSlot(), newTablet);
   }

   public static InteractionHand getHand(Player player) {
      return player.getUsedItemHand() == null ? InteractionHand.MAIN_HAND : player.getUsedItemHand();
   }

   public List<ItemStack> getItemsForTab() {
      return Collections.singletonList(new ItemStack((ItemLike)TabletModule.TABLET.get()));
   }

   @Override
   public void cycle(Player player, ItemStack stack, boolean next) {
      int currentItem = getCurrentSlot(stack);

      for (int tries = 7; tries > 0; tries--) {
         if (next) {
            currentItem = (currentItem + 1) % 6;
         } else {
            currentItem = (currentItem + 6 - 1) % 6;
         }

         ItemStack containingItem = getContainingItem(stack, currentItem);
         if (!containingItem.isEmpty()) {
            setCurrentSlot(player, stack, currentItem);
            player.sendSystemMessage(ComponentFactory.literal("Switched item"));
            return;
         }
      }
   }

   public static ItemStack getContainingItem(ItemStack stack, int slot) {
      TabletData data = (TabletData)stack.getOrDefault(TabletModule.ITEM_TABLET_DATA, TabletData.EMPTY);
      return slot >= 0 && slot < data.stacks().size() ? data.stacks().get(slot) : ItemStack.EMPTY;
   }

   public static void setContainingItem(Player player, InteractionHand hand, int slot, ItemStack containingItem) {
      ItemStack stack = player.getItemInHand(hand);
      TabletData data = (TabletData)stack.getOrDefault(TabletModule.ITEM_TABLET_DATA, TabletData.EMPTY);
      if (containingItem.isEmpty()) {
         data = data.setStack(slot, ItemStack.EMPTY);
      } else {
         data = data.setStack(slot, containingItem.copy());
      }

      stack.set(TabletModule.ITEM_TABLET_DATA, data);
      int current = getCurrentSlot(stack);
      ItemStack newTablet = deriveNewItemstack(slot, containingItem, data, current);
      player.getInventory().getNonEquipmentItems().set(player.getInventory().getSelectedSlot(), newTablet);
   }

   private static ItemStack deriveNewItemstack(int slot, ItemStack containingItem, TabletData data, int current) {
      ItemStack newTablet;
      if (slot == current) {
         if (containingItem.isEmpty()) {
            newTablet = new ItemStack((ItemLike)TabletModule.TABLET.get());
         } else {
            newTablet = new ItemStack(((ITabletSupport)containingItem.getItem()).getInstalledTablet());
         }
      } else {
         newTablet = new ItemStack((ItemLike)TabletModule.TABLET.get());
      }

      newTablet.set(TabletModule.ITEM_TABLET_DATA, data);
      return newTablet;
   }

   @Nonnull
   public InteractionResult use(Level world, Player player, @Nonnull InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!world.isClientSide()) {
         if (player.isShiftKeyDown()) {
            this.openTabletGui(player);
         } else {
            ItemStack containingItem = getContainingItem(stack, getCurrentSlot(stack));
            if (containingItem.isEmpty()) {
               this.openTabletGui(player);
            } else if (containingItem.getItem() instanceof ITabletSupport) {
               ((ITabletSupport)containingItem.getItem()).openGui(player, stack, containingItem);
            }
         }

         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.SUCCESS;
      }
   }

   private void openTabletGui(Player player) {
      player.openMenu(new MenuProvider() {
         {
            Objects.requireNonNull(TabletItem.this);
         }

         @Nonnull
         public Component getDisplayName() {
            return ComponentFactory.literal("Tablet");
         }

         public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory, @Nonnull Player playerx) {
            TabletContainer container = new TabletContainer(id, playerx.blockPosition(), playerx);
            container.setupInventories(new TabletItemHandler(playerx), playerInventory);
            return container;
         }
      });
   }

   public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
      return false;
   }

   public void appendHoverText(
      @Nonnull ItemStack itemStack, TooltipContext context, TooltipDisplay tooltipDisplay, @Nonnull Consumer<Component> list, @Nonnull TooltipFlag flags
   ) {
      super.appendHoverText(itemStack, context, tooltipDisplay, list, flags);
      ((TooltipBuilder)this.tooltipBuilder.get()).makeTooltip(Tools.getId(this), itemStack, list, flags);
   }
}
