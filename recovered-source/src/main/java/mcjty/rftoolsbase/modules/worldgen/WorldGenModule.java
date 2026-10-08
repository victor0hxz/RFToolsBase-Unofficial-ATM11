package mcjty.rftoolsbase.modules.worldgen;

import mcjty.lib.datagen.DataGen;
import mcjty.lib.modules.IModule;
import mcjty.lib.varia.TagTools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.worldgen.blocks.DimensionalShardBlock;
import mcjty.rftoolsbase.setup.Registration;
import mcjty.rftoolsbase.worldgen.OreGenerator;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public class WorldGenModule implements IModule {
   public static final DeferredBlock<Block> DIMENSIONAL_SHARD_OVERWORLD = Registration.BLOCKS
      .register("dimensionalshard_overworld", DimensionalShardBlock::new);
   public static final DeferredItem<Item> DIMENSIONAL_SHARD_OVERWORLD_ITEM = Registration.ITEMS
      .register(
         "dimensionalshard_overworld", RFToolsBase.tab(() -> new BlockItem((Block)DIMENSIONAL_SHARD_OVERWORLD.get(), Registration.createStandardProperties()))
      );
   public static final DeferredBlock<Block> DIMENSIONAL_SHARD_NETHER = Registration.BLOCKS.register("dimensionalshard_nether", DimensionalShardBlock::new);
   public static final DeferredItem<Item> DIMENSIONAL_SHARD_NETHER_ITEM = Registration.ITEMS
      .register("dimensionalshard_nether", RFToolsBase.tab(() -> new BlockItem((Block)DIMENSIONAL_SHARD_NETHER.get(), Registration.createStandardProperties())));
   public static final DeferredBlock<Block> DIMENSIONAL_SHARD_END = Registration.BLOCKS.register("dimensionalshard_end", DimensionalShardBlock::new);
   public static final DeferredItem<Item> DIMENSIONAL_SHARD_END_ITEM = Registration.ITEMS
      .register("dimensionalshard_end", RFToolsBase.tab(() -> new BlockItem((Block)DIMENSIONAL_SHARD_END.get(), Registration.createStandardProperties())));
   public static final TagKey<Block> DIMENSIONAL_SHARD_ORE = TagTools.createBlockTagKey(Identifier.fromNamespaceAndPath("c", "ores/dimensional_shard"));
   public static final TagKey<Item> DIMENSIONAL_SHARD_ORE_ITEM = TagTools.createItemTagKey(Identifier.fromNamespaceAndPath("c", "ores/dimensional_shard"));

   public WorldGenModule() {
      OreGenerator.init();
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
