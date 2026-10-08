package mcjty.rftoolsbase.modules.filter;

import java.util.function.Supplier;
import mcjty.lib.datagen.DataGen;
import mcjty.lib.modules.IModule;
import mcjty.lib.varia.SafeClientTools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.filter.client.GuiFilterModule;
import mcjty.rftoolsbase.modules.filter.data.FilterModuleData;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleItem;
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

public class FilterModule implements IModule {
   public static final DeferredItem<FilterModuleItem> FILTER_MODULE = Registration.ITEMS.register("filter_module", RFToolsBase.tab(FilterModuleItem::new));
   public static final Supplier<MenuType<FilterModuleContainer>> CONTAINER_FILTER_MODULE = Registration.CONTAINERS
      .register("filter_module", FilterModule::createFilterModuleContainer);
   public static final DeferredHolder<DataComponentType<?>, DataComponentType<FilterModuleData>> ITEM_FILTERMODULE_DATA = Registration.COMPONENTS
      .registerComponentType("filtermodule_data", builder -> builder.persistent(FilterModuleData.CODEC).networkSynchronized(FilterModuleData.STREAM_CODEC));

   private static MenuType<FilterModuleContainer> createFilterModuleContainer() {
      return IMenuTypeExtension.create((windowId, inv, data) -> {
         Player player = SafeClientTools.getClientPlayer();
         FilterModuleContainer container = new FilterModuleContainer(windowId, player.blockPosition(), player);
         container.setupInventories(null, inv);
         return container;
      });
   }

   public FilterModule(IEventBus bus) {
      bus.addListener(this::registerMenuScreens);
   }

   public void init(FMLCommonSetupEvent event) {
   }

   public void initClient(FMLClientSetupEvent event) {
   }

   public void registerMenuScreens(RegisterMenuScreensEvent event) {
      GuiFilterModule.register(event);
   }

   public void initConfig(IEventBus bus) {
   }

   public void initDatagen(DataGen dataGen, Provider provider) {
   }
}
