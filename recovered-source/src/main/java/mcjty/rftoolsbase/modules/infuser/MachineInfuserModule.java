package mcjty.rftoolsbase.modules.infuser;

import java.util.function.Supplier;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.blocks.RBlock;
import mcjty.lib.builder.BlockBuilder;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.datagen.DataGen;
import mcjty.lib.items.BaseBlockItem;
import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.modules.infuser.blocks.MachineInfuserTileEntity;
import mcjty.rftoolsbase.modules.infuser.client.GuiMachineInfuser;
import mcjty.rftoolsbase.modules.infuser.data.InfuserData;
import mcjty.rftoolsbase.setup.Config;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MachineInfuserModule implements IModule {
   public static final RBlock<BaseBlock, BlockItem, MachineInfuserTileEntity> MACHINE_INFUSER = Registration.RBLOCKS
      .registerBlock(
         "machine_infuser",
         MachineInfuserTileEntity.class,
         () -> new BaseBlock(new BlockBuilder().tileEntitySupplier(MachineInfuserTileEntity::new)),
         block -> new BaseBlockItem((Block)block.get(), Registration.createStandardProperties()),
         MachineInfuserTileEntity::new
      );
   public static final Supplier<MenuType<GenericContainer>> CONTAINER_MACHINE_INFUSER = Registration.CONTAINERS
      .register("machine_infuser", GenericContainer::createContainerType);
   public static final Supplier<AttachmentType<InfuserData>> INFUSER_DATA = Registration.ATTACHMENT_TYPES
      .register("infuser_data", () -> AttachmentType.builder(() -> new InfuserData(0)).serialize(InfuserData.CODEC.fieldOf("data")).build());
   public static final DeferredHolder<DataComponentType<?>, DataComponentType<InfuserData>> ITEM_INFUSER_DATA = Registration.COMPONENTS
      .registerComponentType("infuser_data", builder -> builder.persistent(InfuserData.CODEC).networkSynchronized(InfuserData.STREAM_CODEC));

   public MachineInfuserModule(IEventBus bus) {
      bus.addListener(this::registerScreens);
   }

   public void init(FMLCommonSetupEvent event) {
   }

   public void initClient(FMLClientSetupEvent event) {
   }

   public void registerScreens(RegisterMenuScreensEvent event) {
      GuiMachineInfuser.register(event);
   }

   public void initConfig(IEventBus bus) {
      MachineInfuserConfiguration.init(Config.SERVER_BUILDER);
   }

   public void initDatagen(DataGen dataGen, Provider provider) {
   }
}
