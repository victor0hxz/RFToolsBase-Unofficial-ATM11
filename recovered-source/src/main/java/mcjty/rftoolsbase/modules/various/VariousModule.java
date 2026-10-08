package mcjty.rftoolsbase.modules.various;

import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mcjty.lib.datagen.DataGen;
import mcjty.lib.modules.IModule;
import mcjty.lib.varia.TagTools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.data.WrenchData;
import mcjty.rftoolsbase.modules.various.items.ManualItem;
import mcjty.rftoolsbase.modules.various.items.SmartWrenchItem;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public class VariousModule implements IModule {
   public static final DeferredItem<SmartWrenchItem> SMARTWRENCH = Registration.ITEMS
      .register("smartwrench", RFToolsBase.tab(() -> new SmartWrenchItem(SmartWrenchMode.MODE_WRENCH)));
   public static final DeferredItem<SmartWrenchItem> SMARTWRENCH_SELECT = Registration.ITEMS
      .register("smartwrench_select", RFToolsBase.tab(() -> new SmartWrenchItem(SmartWrenchMode.MODE_SELECT)));
   public static final DeferredItem<Item> DIMENSIONALSHARD = Registration.ITEMS
      .register("dimensionalshard", RFToolsBase.tab(VariousModule::createDimensionalShard));
   public static final DeferredItem<Item> INFUSED_DIAMOND = Registration.ITEMS.register("infused_diamond", RFToolsBase.tab(VariousModule::createItem16));
   public static final DeferredItem<Item> INFUSED_ENDERPEARL = Registration.ITEMS.register("infused_enderpearl", RFToolsBase.tab(VariousModule::createItem16));
   public static final DeferredItem<Item> MACHINE_FRAME = Registration.ITEMS
      .register("machine_frame", RFToolsBase.tab(() -> new Item(Registration.createStandardProperties())));
   public static final DeferredItem<Item> MACHINE_BASE = Registration.ITEMS
      .register("machine_base", RFToolsBase.tab(() -> new Item(Registration.createStandardProperties())));
   public static final DeferredItem<ManualItem> MANUAL = Registration.ITEMS.register("manual", RFToolsBase.tab(ManualItem::new));
   public static final Identifier SHARDS = Identifier.fromNamespaceAndPath("rftoolsbase", "shards");
   public static final TagKey<Item> SHARDS_TAG = TagTools.createItemTagKey(SHARDS);
   public static final DeferredHolder<DataComponentType<?>, DataComponentType<WrenchData>> ITEM_WRENCH_DATA = Registration.COMPONENTS
      .registerComponentType("wrench_data", builder -> builder.persistent(WrenchData.CODEC).networkSynchronized(WrenchData.STREAM_CODEC));

   private static Item createItem16() {
      return new Item(RFToolsBase.setup.defaultProperties().stacksTo(16));
   }

   private static Item createDimensionalShard() {
      return new Item(RFToolsBase.setup.defaultProperties().stacksTo(64));
   }

   public void init(FMLCommonSetupEvent event) {
   }

   public void initClient(FMLClientSetupEvent event) {
   }

   public void initConfig(IEventBus bus) {
   }

   public void initDatagen(DataGen dataGen, Provider provider) {
   }
}
