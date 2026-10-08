package mcjty.rftoolsbase.modules.crafting;

import java.util.function.Supplier;
import mcjty.lib.datagen.DataGen;
import mcjty.lib.modules.IModule;
import mcjty.lib.varia.SafeClientTools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.crafting.client.GuiCraftingCard;
import mcjty.rftoolsbase.modules.crafting.data.CraftingCardData;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public class CraftingModule implements IModule {
   public static final DeferredItem<Item> CRAFTING_CARD = Registration.ITEMS.register("crafting_card", RFToolsBase.tab(CraftingCardItem::new));
   public static final Supplier<MenuType<CraftingCardContainer>> CONTAINER_CRAFTING_CARD = Registration.CONTAINERS
      .register("crafting_card", CraftingModule::createCraftingContainer);
   public static final DeferredHolder<DataComponentType<?>, DataComponentType<CraftingCardData>> ITEM_CRAFTINGCARD_DATA = Registration.COMPONENTS
      .registerComponentType("craftingcard_data", builder -> builder.persistent(CraftingCardData.CODEC).networkSynchronized(CraftingCardData.STREAM_CODEC));

   private static MenuType<CraftingCardContainer> createCraftingContainer() {
      return IMenuTypeExtension.create((windowId, inv, data) -> {
         Player player = SafeClientTools.getClientPlayer();
         CraftingCardContainer container = new CraftingCardContainer(windowId, player.blockPosition(), player);
         container.setupInventories(null, inv);
         return container;
      });
   }

   public CraftingModule(IEventBus bus) {
      bus.addListener(this::registerMenuScreens);
   }

   public void init(FMLCommonSetupEvent event) {
   }

   public void initClient(FMLClientSetupEvent event) {
   }

   public void registerMenuScreens(RegisterMenuScreensEvent event) {
      GuiCraftingCard.register(event);
   }

   public void initConfig(IEventBus bus) {
   }

   public void initDatagen(DataGen dataGen, Provider provider) {
   }
}
