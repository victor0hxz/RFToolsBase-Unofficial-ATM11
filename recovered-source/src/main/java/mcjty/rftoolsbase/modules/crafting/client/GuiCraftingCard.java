package mcjty.rftoolsbase.modules.crafting.client;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.events.BlockRenderEvent;
import mcjty.lib.gui.layout.HorizontalAlignment;
import mcjty.lib.gui.layout.PositionalLayout.PositionalHint;
import mcjty.lib.gui.widgets.BlockRender;
import mcjty.lib.gui.widgets.Button;
import mcjty.lib.gui.widgets.Label;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.Widget;
import mcjty.lib.gui.widgets.Widgets;
import mcjty.lib.network.PacketSendServerCommand;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.ItemStackList;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import mcjty.rftoolsbase.modules.crafting.network.PacketItemComponentsToServer;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.TooltipFlag.Default;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class GuiCraftingCard extends GenericGuiContainer<GenericTileEntity, CraftingCardContainer> {
   public static final int WIDTH = 180;
   public static final int HEIGHT = 198;
   private static final Identifier iconLocation = Identifier.fromNamespaceAndPath("rftoolsbase", "textures/gui/craftingcard.png");
   private final BlockRender[] slots = new BlockRender[21];

   public GuiCraftingCard(CraftingCardContainer container, Inventory inventory, Component title) {
      super(container, inventory, title, CraftingCardItem.MANUAL, 180, 198);
   }

   public static void register(RegisterMenuScreensEvent event) {
      event.register(CraftingModule.CONTAINER_CRAFTING_CARD.get(), GuiCraftingCard::new);
   }

   public void init() {
      super.init();
      Panel toplevel = (Panel)Widgets.positional().background(iconLocation);
      toplevel.bounds(this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
      toplevel.children(
         new Widget[]{((Label)Widgets.label("Regular 3x3 crafting recipe").horizontalAlignment(HorizontalAlignment.ALIGN_LEFT)).hint(10, 4, 160, 14)}
      );
      toplevel.children(
         new Widget[]{((Label)Widgets.label("or more complicated recipes").horizontalAlignment(HorizontalAlignment.ALIGN_LEFT)).hint(10, 17, 160, 14)}
      );
      toplevel.children(
         new Widget[]{
            ((Button)Widgets.button(110, 57, 60, 14, "Update").tooltips(new String[]{"Update the item in the output", "slot to the recipe in the", "3x3 grid"}))
               .event(() -> RFToolsBaseMessages.sendToServer(PacketSendServerCommand.create("rftoolsbase", "testRecipe", TypedMap.EMPTY)))
         }
      );

      for (int y = 0; y < 4; y++) {
         for (int x = 0; x < 5; x++) {
            int idx = y * 5 + x;
            this.createDummySlot(toplevel, idx, new PositionalHint(x * 18 + 10, y * 18 + 37, 18, 18), this.createSelectionEvent(idx));
         }
      }

      this.createDummySlot(toplevel, 20, new PositionalHint(154, 37, 18, 18), this.createSelectionEvent(20));
      this.updateSlots();
      this.window = new Window(this, toplevel);
   }

   private void createDummySlot(Panel toplevel, final int idx, PositionalHint hint, BlockRenderEvent selectionEvent) {
      this.slots[idx] = (BlockRender)(new BlockRender() {
         {
            Objects.requireNonNull(GuiCraftingCard.this);
         }

         public List<String> getTooltips() {
            if (!(GuiCraftingCard.this.slots[idx].getRenderItem() instanceof ItemStack stack)) {
               return Collections.emptyList();
            } else if (stack.isEmpty()) {
               return Collections.emptyList();
            } else {
               TooltipFlag flag = this.mc.options.advancedItemTooltips ? Default.ADVANCED : Default.NORMAL;
               List<Component> list = stack.getTooltipLines(TooltipContext.of(this.mc.level), this.mc.player, flag);
               int i = 0;

               while (i < list.size()) {
                  i++;
               }

               return list.stream().<String>map(Component::getString).collect(Collectors.toList());
            }
         }
      }).hilightOnHover(true).hint(hint);
      this.slots[idx].event(selectionEvent);
      toplevel.children(new Widget[]{this.slots[idx]});
   }

   private void updateSlots() {
      ItemStackList stacks = this.getStacks();
      if (!stacks.isEmpty()) {
         for (int i = 0; i < stacks.size(); i++) {
            this.slots[i].renderItem(stacks.get(i));
         }
      }
   }

   @Nonnull
   private ItemStackList getStacks() {
      ItemStack cardItem = this.minecraft.player.getItemInHand(InteractionHand.MAIN_HAND);
      ItemStackList stacks = ItemStackList.EMPTY;
      if (!cardItem.isEmpty() && cardItem.getItem() instanceof CraftingCardItem) {
         stacks = CraftingCardItem.getStacksFromItem(cardItem);
      }

      return stacks;
   }

   private BlockRenderEvent createSelectionEvent(final int idx) {
      return new BlockRenderEvent() {
         {
            Objects.requireNonNull(GuiCraftingCard.this);
         }

         public void select() {
            ItemStack itemstack = ((CraftingCardContainer)GuiCraftingCard.this.menu).getCarried();
            GuiCraftingCard.this.slots[idx].renderItem(itemstack);
            ItemStackList stacks = GuiCraftingCard.this.getStacks();
            if (!stacks.isEmpty()) {
               stacks.set(idx, itemstack);
               ItemStack cardItem = GuiCraftingCard.this.minecraft.player.getItemInHand(InteractionHand.MAIN_HAND);
               CraftingCardItem.putStacksInItem(cardItem, stacks);
               RFToolsBaseMessages.sendToServer(PacketItemComponentsToServer.create(cardItem));
            }
         }

         public void doubleClick() {
         }
      };
   }

   public void extractBackground(@Nonnull GuiGraphicsExtractor graphics, int x, int y, float partialTicks) {
      this.updateSlots();
      this.drawWindow(graphics, partialTicks, x, y);
   }
}
