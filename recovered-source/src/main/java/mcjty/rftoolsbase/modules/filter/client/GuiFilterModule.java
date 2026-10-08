package mcjty.rftoolsbase.modules.filter.client;

import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.TagSelectorWindow;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.layout.HorizontalAlignment;
import mcjty.lib.gui.widgets.BlockRender;
import mcjty.lib.gui.widgets.Button;
import mcjty.lib.gui.widgets.ImageChoiceLabel;
import mcjty.lib.gui.widgets.Label;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.Slider;
import mcjty.lib.gui.widgets.Widget;
import mcjty.lib.gui.widgets.WidgetList;
import mcjty.lib.gui.widgets.Widgets;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.lib.varia.ComponentFactory;
import mcjty.lib.varia.SafeClientTools;
import mcjty.lib.varia.TagTools;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import mcjty.rftoolsbase.modules.filter.data.FilterModuleData;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleInventory;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleItem;
import mcjty.rftoolsbase.modules.filter.network.PacketSyncHandItem;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class GuiFilterModule extends GenericGuiContainer<GenericTileEntity, FilterModuleContainer> {
   public static final int CONTROLLER_WIDTH = 230;
   public static final int CONTROLLER_HEIGHT = 188;
   private static final Identifier iconLocation = Identifier.fromNamespaceAndPath("rftoolsbase", "textures/gui/filtermodule.png");
   private static final Identifier guiElements = Identifier.fromNamespaceAndPath("rftoolsbase", "textures/gui/guielements.png");
   private final TagSelectorWindow selector = new TagSelectorWindow();
   private ImageChoiceLabel blacklistMode;
   private ImageChoiceLabel damageMode;
   private ImageChoiceLabel componentMode;
   private ImageChoiceLabel modMode;
   private Button remove;
   private Button expand;
   private WidgetList list;

   public GuiFilterModule(FilterModuleContainer container, Inventory inventory, Component title) {
      super(container, inventory, title, FilterModuleItem.MANUAL, 230, 188);
   }

   public static void register(RegisterMenuScreensEvent event) {
      event.register(FilterModule.CONTAINER_FILTER_MODULE.get(), GuiFilterModule::new);
   }

   public void init() {
      super.init();
      this.remove = ((Button)Widgets.button(5, 106, 50, 15, "Remove").tooltips(new String[]{"Remove current selection"})).event(this::removeSelection);
      this.expand = ((Button)Widgets.button(5, 121, 50, 15, "Expand").tooltips(new String[]{"Expand item to tags"})).event(this::expandToTags);
      Button addTags = ((Button)Widgets.button(5, 137, 50, 15, "Add tags").tooltips(new String[]{"Add tags"})).event(this::addTagWindow);
      this.blacklistMode = ((ImageChoiceLabel)Widgets.imageChoice(5, 152, 16, 16).tooltips(new String[]{"Black or whitelist mode"}))
         .event(newChoice -> this.updateSettings());
      this.blacklistMode.choice("Black", "Blacklist items", guiElements, 224, 32);
      this.blacklistMode.choice("White", "Whitelist items", guiElements, 240, 32);
      this.damageMode = ((ImageChoiceLabel)Widgets.imageChoice(21, 152, 16, 16).tooltips(new String[]{"Filter ignoring damage"}))
         .event(newChoice -> this.updateSettings());
      this.damageMode.choice("Off", "Ignore damage", guiElements, 96, 32);
      this.damageMode.choice("On", "Damage must match", guiElements, 112, 32);
      this.componentMode = ((ImageChoiceLabel)Widgets.imageChoice(5, 168, 16, 16).tooltips(new String[]{"Filter ignoring Components"}))
         .event(newChoice -> this.updateSettings());
      this.componentMode.choice("Off", "Ignore Components", guiElements, 128, 32);
      this.componentMode.choice("On", "Components must match", guiElements, 144, 32);
      this.modMode = ((ImageChoiceLabel)Widgets.imageChoice(21, 168, 16, 16).tooltips(new String[]{"Filter ignoring mod"}))
         .event(newChoice -> this.updateSettings());
      this.modMode.choice("Off", "Don't match on mod", guiElements, 192, 32);
      this.modMode.choice("On", "Only mod must match", guiElements, 208, 32);
      this.list = (WidgetList)Widgets.list(5, 4, 207, 99).name("list");
      Slider slider = Widgets.slider(212, 4, 10, 99).scrollableName("list");
      ItemStack stack = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND);
      FilterModuleData data = (FilterModuleData)stack.getOrDefault(FilterModule.ITEM_FILTERMODULE_DATA, FilterModuleData.EMPTY);
      this.setBlacklistMode(data.blacklist() ? "Black" : "White");
      this.damageMode.setCurrentChoice(data.damage() ? 1 : 0);
      this.componentMode.setCurrentChoice(data.components() ? 1 : 0);
      this.modMode.setCurrentChoice(data.mod() ? 1 : 0);
      Panel toplevel = (Panel)((Panel)Widgets.positional().background(iconLocation))
         .children(new Widget[]{this.blacklistMode, this.damageMode, this.componentMode, this.modMode, this.list, slider, this.remove, this.expand, addTags});
      toplevel.bounds(this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
      this.window = new Window(this, toplevel);
      this.fillList();
   }

   private void addTagWindow() {
      this.selector.create(this.window, "both", t -> {
         if (t != null) {
            FilterModuleInventory inventory = new FilterModuleInventory(Minecraft.getInstance().player);
            inventory.addTag(TagTools.createItemTagKey(Identifier.parse(t)));
            inventory.markDirty();
            this.refresh();
         }
      }, () -> null, true);
   }

   private void refresh() {
      this.syncStack();
      this.fillList();
   }

   private void expandToTags() {
      if (this.canExpand()) {
         FilterModuleInventory inventory = new FilterModuleInventory(Minecraft.getInstance().player);
         ItemStack stack = inventory.getStacks().get(this.list.getSelected() - inventory.getTags().size());
         Collection<TagKey<Item>> tags = TagTools.getTags(stack.getItem());
         if (!tags.isEmpty()) {
            this.removeSelection();
            inventory = new FilterModuleInventory(Minecraft.getInstance().player);

            for (TagKey<Item> tag : tags) {
               inventory.addTag(tag);
            }

            inventory.markDirty();
            this.refresh();
         }
      }
   }

   private void removeSelection() {
      if (this.canRemove()) {
         FilterModuleInventory inventory = new FilterModuleInventory(Minecraft.getInstance().player);
         if (this.list.getSelected() >= inventory.getTags().size()) {
            inventory.removeStack(this.list.getSelected() - inventory.getTags().size());
         } else {
            Identifier location = (Identifier)this.list.getChild(this.list.getSelected()).getUserObject();
            TagKey<Item> tag = TagTools.createItemTagKey(location);
            inventory.removeTag(tag);
         }

         inventory.markDirty();
         this.refresh();
      }
   }

   protected void drawWindow(GuiGraphicsExtractor graphics, float partialTicks, int x, int y) {
      super.drawWindow(graphics, partialTicks, x, y);
      this.remove.enabled(this.canRemove());
      this.expand.enabled(this.canExpand());
   }

   private boolean canRemove() {
      return this.list.getSelected() != -1;
   }

   private boolean canExpand() {
      return this.list.getSelected() != -1 && this.list.getChild(this.list.getSelected()).getUserObject() == null;
   }

   private void fillList() {
      FilterModuleInventory inventory = new FilterModuleInventory(Minecraft.getInstance().player);
      this.list.removeChildren();

      for (TagKey<Item> tag : inventory.getTags()) {
         this.addTagToList(tag);
      }

      for (ItemStack stack : inventory.getStacks()) {
         Panel panel = Widgets.horizontal();
         BlockRender render = new BlockRender().renderItem(stack);
         panel.children(new Widget[]{render});
         String formattedText = stack.getHoverName().getString();
         if (formattedText.length() >= 30) {
            formattedText = formattedText.substring(0, 28) + "...";
         }

         panel.children(new Widget[]{Widgets.label(formattedText)});
         this.list.children(new Widget[]{panel});
      }
   }

   private void addTagToList(TagKey<Item> tag) {
      Panel panel = Widgets.horizontal(0, 0);
      panel.userObject(tag.location());
      panel.children(new Widget[]{((Label)Widgets.label(tag.location().toString()).desiredWidth(120)).horizontalAlignment(HorizontalAlignment.ALIGN_LEFT)});
      int i = 5;

      for (Holder<Item> item : TagTools.getItemsForTag(tag)) {
         BlockRender render = new BlockRender().renderItem(new ItemStack(item));
         panel.children(new Widget[]{render});
         if (--i <= 0) {
            break;
         }
      }

      this.list.children(new Widget[]{panel});
   }

   private void setBlacklistMode(String mode) {
      int idx = this.blacklistMode.findChoice(mode);
      if (idx == -1) {
         this.blacklistMode.setCurrentChoice("Black");
      } else {
         this.blacklistMode.setCurrentChoice(idx);
      }
   }

   protected void slotClicked(Slot slotIn, int slotId, int mouseButton, @Nonnull ContainerInput type) {
      if (slotIn != null) {
         if (!slotIn.getItem().isEmpty()) {
            FilterModuleInventory inventory = new FilterModuleInventory(this.minecraft.player);
            if (SafeClientTools.isSneaking()) {
               TagTools.getTags(slotIn.getItem().getItem()).forEach(inventory::addTag);
            } else {
               inventory.addStack(slotIn.getItem());
            }

            inventory.markDirty();
            this.refresh();
         }
      }
   }

   protected List<Component> getTooltipFromContainerItem(ItemStack stack) {
      List<Component> list = getTooltipFromItem(this.minecraft, stack);
      list.add(ComponentFactory.literal(ChatFormatting.GOLD + "Click to add to filter"));
      list.add(ComponentFactory.literal(ChatFormatting.GOLD + "Shift-Click to add tags to filter"));
      return list;
   }

   private void syncStack() {
      RFToolsBaseMessages.sendToServer(PacketSyncHandItem.create(this.minecraft.player));
   }

   private void updateSettings() {
      ItemStack stack = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND);
      FilterModuleData data = (FilterModuleData)stack.getOrDefault(FilterModule.ITEM_FILTERMODULE_DATA, FilterModuleData.EMPTY);
      data = new FilterModuleData(
         data.stacks(),
         data.tags(),
         this.blacklistMode.getCurrentChoice().equals("Black"),
         this.damageMode.getCurrentChoiceIndex() == 1,
         this.componentMode.getCurrentChoiceIndex() == 1,
         this.modMode.getCurrentChoiceIndex() == 1
      );
      stack.set(FilterModule.ITEM_FILTERMODULE_DATA, data);
      RFToolsBaseMessages.sendToServer(PacketSyncHandItem.create(this.minecraft.player));
   }
}
