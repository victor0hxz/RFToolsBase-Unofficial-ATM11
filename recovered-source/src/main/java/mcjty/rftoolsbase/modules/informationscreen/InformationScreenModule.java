package mcjty.rftoolsbase.modules.informationscreen;

import java.util.function.Supplier;
import mcjty.lib.datagen.DataGen;
import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenBlock;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import mcjty.rftoolsbase.modules.informationscreen.client.InformationScreenRenderer;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public class InformationScreenModule implements IModule {
   public static final DeferredBlock<Block> INFORMATION_SCREEN = Registration.BLOCKS.register("information_screen", InformationScreenBlock::new);
   public static final DeferredItem<Item> INFORMATION_SCREEN_ITEM = Registration.ITEMS
      .register("information_screen", RFToolsBase.tab(() -> new BlockItem((Block)INFORMATION_SCREEN.get(), Registration.createStandardProperties())));
   public static final Supplier<BlockEntityType<InformationScreenTileEntity>> TYPE_INFORMATION_SCREEN = Registration.TILES
      .register("information_screen", () -> new BlockEntityType(InformationScreenTileEntity::new, new Block[]{(Block)INFORMATION_SCREEN.get()}));

   public void init(FMLCommonSetupEvent event) {
   }

   public void initClient(FMLClientSetupEvent event) {
      InformationScreenRenderer.register();
   }

   public void initConfig(IEventBus bus) {
   }

   public void initDatagen(DataGen dataGen, Provider provider) {
   }
}
