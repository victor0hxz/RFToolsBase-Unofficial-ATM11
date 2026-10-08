package mcjty.rftoolsbase.modules.tablet;

import java.util.function.Supplier;
import mcjty.lib.datagen.DataGen;
import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.tablet.client.GuiTablet;
import mcjty.rftoolsbase.modules.tablet.data.TabletData;
import mcjty.rftoolsbase.modules.tablet.items.TabletContainer;
import mcjty.rftoolsbase.modules.tablet.items.TabletItem;
import mcjty.rftoolsbase.modules.tablet.items.TabletItemHandler;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public class TabletModule implements IModule {
   public static final DeferredItem<TabletItem> TABLET = Registration.ITEMS.register("tablet", RFToolsBase.tab(TabletItem::new));
   public static final Supplier<MenuType<TabletContainer>> CONTAINER_TABLET = Registration.CONTAINERS.register("tablet", TabletModule::createTabletContainer);
   public static final DeferredItem<TabletItem> TABLET_FILLED = Registration.ITEMS.register("tablet_filled", TabletItem::new);
   public static final DeferredHolder<DataComponentType<?>, DataComponentType<TabletData>> ITEM_TABLET_DATA = Registration.COMPONENTS
      .registerComponentType("tablet_data", builder -> builder.persistent(TabletData.CODEC).networkSynchronized(TabletData.STREAM_CODEC));

   private static MenuType<TabletContainer> createTabletContainer() {
      return IMenuTypeExtension.create((windowId, inv, data) -> {
         Player player = inv.player;
         TabletContainer container = new TabletContainer(windowId, player.blockPosition(), player);
         container.setupInventories(new TabletItemHandler(player), inv);
         return container;
      });
   }

   public TabletModule(IEventBus bus) {
      bus.addListener(this::registerScreens);
   }

   public void init(FMLCommonSetupEvent event) {
   }

   public void initClient(FMLClientSetupEvent event) {
   }

   public void registerScreens(RegisterMenuScreensEvent event) {
      GuiTablet.register(event);
   }

   public void initConfig(IEventBus bus) {
   }

   public void initDatagen(DataGen dataGen, Provider provider) {
   }
}
