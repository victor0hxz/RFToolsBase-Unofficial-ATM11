package mcjty.rftoolsbase.modules.tablet.client;

import javax.annotation.Nonnull;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.ToggleButton;
import mcjty.lib.gui.widgets.Widget;
import mcjty.lib.gui.widgets.Widgets;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.rftoolsbase.modules.filter.network.PacketSyncHandItem;
import mcjty.rftoolsbase.modules.tablet.TabletModule;
import mcjty.rftoolsbase.modules.tablet.items.TabletContainer;
import mcjty.rftoolsbase.modules.tablet.items.TabletItem;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class GuiTablet extends GenericGuiContainer<GenericTileEntity, TabletContainer> {
   public static final int TABLET_WIDTH = 180;
   public static final int TABLET_HEIGHT = 188;
   private static final Identifier iconLocation = Identifier.fromNamespaceAndPath("rftoolsbase", "textures/gui/tablet.png");
   private ToggleButton[] buttons;

   public GuiTablet(TabletContainer container, Inventory inventory, Component title) {
      super(container, inventory, title, TabletItem.MANUAL, 180, 188);
   }

   public static void register(RegisterMenuScreensEvent event) {
      event.register(TabletModule.CONTAINER_TABLET.get(), GuiTablet::new);
   }

   public void init() {
      super.init();
      Panel toplevel = (Panel)Widgets.positional().background(iconLocation);
      this.buttons = new ToggleButton[6];

      for (int i = 0; i < 6; i++) {
         int finalI = i;
         this.buttons[i] = ((ToggleButton)new ToggleButton().hint(14 + i * 23, 32, 19, 8)).event(() -> this.setActive(finalI));
         toplevel.children(new Widget[]{this.buttons[i]});
      }

      toplevel.bounds(this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
      this.window = new Window(this, toplevel);
   }

   private void updateActiveButton(int current) {
      for (int i = 0; i < 6; i++) {
         this.buttons[i].pressed(i == current);
      }
   }

   private InteractionHand getHand() {
      return this.minecraft.player != null && this.minecraft.player.getUsedItemHand() != null
         ? this.minecraft.player.getUsedItemHand()
         : InteractionHand.MAIN_HAND;
   }

   protected void extractLabels(@Nonnull GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
      super.extractLabels(graphics, mouseX, mouseY);
      ItemStack heldItem = this.minecraft.player.getItemInHand(this.getHand());
      this.updateActiveButton(TabletItem.getCurrentSlot(heldItem));
   }

   private void setActive(int i) {
      ItemStack heldItem = this.minecraft.player.getItemInHand(this.getHand());
      TabletItem.setCurrentSlot(this.minecraft.player, heldItem, i);
      this.updateActiveButton(i);
      this.syncStack();
   }

   private void syncStack() {
      RFToolsBaseMessages.sendToServer(PacketSyncHandItem.create(this.minecraft.player));
   }
}
