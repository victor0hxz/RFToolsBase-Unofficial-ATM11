package mcjty.rftoolsbase;

import java.util.function.Supplier;
import mcjty.lib.datagen.DataGen;
import mcjty.lib.modules.Modules;
import mcjty.rftoolsbase.client.ClientInfo;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenModule;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserModule;
import mcjty.rftoolsbase.modules.tablet.TabletModule;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import mcjty.rftoolsbase.setup.ClientSetup;
import mcjty.rftoolsbase.setup.Config;
import mcjty.rftoolsbase.setup.ModSetup;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import mcjty.rftoolsbase.setup.Registration;
import mcjty.rftoolsbase.tools.TickOrderHandler;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent.Client;

@Mod("rftoolsbase")
public class RFToolsBase {
   public static final String MODID = "rftoolsbase";
   public static final ModSetup setup = new ModSetup();
   public static RFToolsBase instance;
   private final Modules modules = new Modules();
   public ClientInfo clientInfo = new ClientInfo();

   public RFToolsBase(ModContainer mod, IEventBus bus, Dist dist) {
      instance = this;
      this.setupModules(bus);
      Config.register(mod, bus, this.modules);
      Registration.register(bus);
      bus.addListener(setup::init);
      bus.addListener(this.modules::init);
      bus.addListener(this::onDataGen);
      bus.addListener(RFToolsBaseMessages::registerMessages);
      bus.addListener(setup.getBlockCapabilityRegistrar(Registration.RBLOCKS));
      NeoForge.EVENT_BUS.addListener(e -> TickOrderHandler.clean());
      if (dist.isClient()) {
         bus.addListener(RFToolsBaseMessages::registerClientMessages);
         bus.addListener(ClientSetup::init);
         bus.addListener(ClientSetup::registerKeyBinds);
         bus.addListener(this.modules::initClient);
      }
   }

   public static <T extends Item> Supplier<T> tab(Supplier<T> supplier) {
      return setup.tab(supplier);
   }

   private void onDataGen(Client event) {
      DataGen datagen = new DataGen("rftoolsbase", event);
      this.modules.datagen(datagen, event.getLookupProvider());
      datagen.generate();
   }

   private void setupModules(IEventBus bus) {
      this.modules.register(new CraftingModule(bus));
      this.modules.register(new FilterModule(bus));
      this.modules.register(new InformationScreenModule());
      this.modules.register(new MachineInfuserModule(bus));
      this.modules.register(new TabletModule(bus));
      this.modules.register(new VariousModule());
      this.modules.register(new WorldGenModule());
   }
}
