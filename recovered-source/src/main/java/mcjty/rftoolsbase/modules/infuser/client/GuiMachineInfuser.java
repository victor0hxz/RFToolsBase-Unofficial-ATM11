package mcjty.rftoolsbase.modules.infuser.client;

import javax.annotation.Nonnull;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.widgets.EnergyBar;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.Widget;
import mcjty.lib.gui.widgets.Widgets;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserModule;
import mcjty.rftoolsbase.modules.infuser.blocks.MachineInfuserTileEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class GuiMachineInfuser extends GenericGuiContainer<MachineInfuserTileEntity, GenericContainer> {
   public static final int INFUSER_WIDTH = 180;
   public static final int INFUSER_HEIGHT = 152;
   private EnergyBar energyBar;
   private static final Identifier iconLocation = Identifier.fromNamespaceAndPath("rftoolsbase", "textures/gui/infuser.png");

   public GuiMachineInfuser(GenericContainer container, Inventory inventory, Component title) {
      super(container, inventory, title, ((BaseBlock)MachineInfuserModule.MACHINE_INFUSER.block().get()).getManualEntry(), 180, 152);
   }

   public static void register(RegisterMenuScreensEvent event) {
      event.register(MachineInfuserModule.CONTAINER_MACHINE_INFUSER.get(), GuiMachineInfuser::new);
   }

   public void init() {
      super.init();
      this.energyBar = ((EnergyBar)((EnergyBar)new EnergyBar().name("energybar")).vertical().hint(10, 7, 8, 54)).showText(false);
      Panel toplevel = (Panel)((Panel)Widgets.positional().background(iconLocation)).children(new Widget[]{this.energyBar});
      toplevel.bounds(this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
      this.window = new Window(this, toplevel);
      this.initializeFields();
   }

   private void initializeFields() {
      this.energyBar = (EnergyBar)this.window.findChild("energybar");
   }

   private void updateFields() {
      if (this.window != null) {
         this.updateEnergyBar(this.energyBar);
      }
   }

   public void extractBackground(@Nonnull GuiGraphicsExtractor graphics, int x, int y, float partialTicks) {
      this.updateFields();
      this.drawWindow(graphics, partialTicks, x, y);
   }
}
