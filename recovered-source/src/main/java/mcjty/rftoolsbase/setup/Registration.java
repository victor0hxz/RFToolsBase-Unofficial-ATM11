package mcjty.rftoolsbase.setup;

import java.util.function.Supplier;
import mcjty.lib.blocks.RBlockRegistry;
import mcjty.lib.setup.DeferredBlocks;
import mcjty.lib.setup.DeferredItems;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.VariousModule;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredRegister.DataComponents;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;

public class Registration {
   public static final RBlockRegistry RBLOCKS = new RBlockRegistry("rftoolsbase", RFToolsBase.setup::addTabItem);
   public static final DeferredBlocks BLOCKS = DeferredBlocks.create("rftoolsbase");
   public static final DeferredItems ITEMS = DeferredItems.create("rftoolsbase");
   public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, "rftoolsbase");
   public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(BuiltInRegistries.MENU, "rftoolsbase");
   public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "rftoolsbase");
   public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "rftoolsbase");
   public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = Tools.createPlacementRegistry("rftoolsbase");
   public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "rftoolsbase");
   public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(Keys.ATTACHMENT_TYPES, "rftoolsbase");
   public static final DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "rftoolsbase");
   public static Supplier<CreativeModeTab> TAB = TABS.register(
      "rftoolsbase",
      () -> CreativeModeTab.builder()
         .title(Component.translatable("itemGroup.rftoolsbase"))
         .icon(() -> new ItemStack((ItemLike)VariousModule.SMARTWRENCH.get()))
         .withTabsBefore(new ResourceKey[]{CreativeModeTabs.SPAWN_EGGS})
         .displayItems((featureFlags, output) -> RFToolsBase.setup.populateTab(output))
         .build()
   );

   public static void register(IEventBus bus) {
      RBLOCKS.register(bus);
      BLOCKS.register(bus);
      ITEMS.register(bus);
      TILES.register(bus);
      CONTAINERS.register(bus);
      SOUNDS.register(bus);
      ENTITIES.register(bus);
      PLACEMENT_MODIFIERS.register(bus);
      TABS.register(bus);
      ATTACHMENT_TYPES.register(bus);
      COMPONENTS.register(bus);
   }

   public static Properties createStandardProperties() {
      return RFToolsBase.setup.defaultProperties();
   }
}
