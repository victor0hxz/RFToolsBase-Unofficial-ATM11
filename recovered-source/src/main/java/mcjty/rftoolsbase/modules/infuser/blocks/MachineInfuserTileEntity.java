package mcjty.rftoolsbase.modules.infuser.blocks;

import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import mcjty.lib.api.container.DefaultContainerProvider;
import mcjty.lib.api.container.ItemInventory;
import mcjty.lib.api.infusable.DefaultInfusable;
import mcjty.lib.api.infusable.IInfusable;
import mcjty.lib.api.infusable.ItemInfusable;
import mcjty.lib.api.power.ItemEnergy;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.builder.BlockBuilder;
import mcjty.lib.builder.InfoLine;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.container.ContainerFactory;
import mcjty.lib.container.GenericItemHandler;
import mcjty.lib.container.SlotDefinition;
import mcjty.lib.setup.Registration;
import mcjty.lib.tileentity.Cap;
import mcjty.lib.tileentity.CapType;
import mcjty.lib.tileentity.GenericEnergyStorage;
import mcjty.lib.tileentity.TickingTileEntity;
import mcjty.lib.varia.TagTools;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserConfiguration;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserModule;
import mcjty.rftoolsbase.modules.infuser.data.InfuserData;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.tools.ManualHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponentMap.Builder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.Lazy;

public class MachineInfuserTileEntity extends TickingTileEntity {
   public static final int SLOT_SHARDINPUT = 0;
   public static final int SLOT_MACHINEOUTPUT = 1;
   public static final Lazy<ContainerFactory> CONTAINER_FACTORY = Lazy.of(
      () -> new ContainerFactory(2)
         .slot(SlotDefinition.specific(MachineInfuserTileEntity::isShard).in(), 0, 64, 24)
         .slot(SlotDefinition.specific(MachineInfuserTileEntity::isInfusable).in().out(), 1, 118, 24)
         .playerSlots(10, 70)
   );
   private final GenericItemHandler items = GenericItemHandler.create(this, CONTAINER_FACTORY)
      .slotLimit(slot -> slot == 1 ? 1 : 64)
      .insertable((slot, stack) -> slot == 1 ? isInfusable(stack) : isShard(stack))
      .build();
   @Cap(type = CapType.ITEMS_AUTOMATION)
   private static final Function<MachineInfuserTileEntity, GenericItemHandler> ITEM_HANDLER = be -> be.items;
   private final GenericEnergyStorage energyStorage = new GenericEnergyStorage(
      this, true, ((Integer)MachineInfuserConfiguration.MAXENERGY.get()).intValue(), ((Integer)MachineInfuserConfiguration.RECEIVEPERTICK.get()).intValue()
   );
   @Cap(type = CapType.ENERGY)
   private static final Function<MachineInfuserTileEntity, GenericEnergyStorage> ENERGY_HANDLER = be -> be.energyStorage;
   @Cap(type = CapType.CONTAINER)
   private static final Function<MachineInfuserTileEntity, MenuProvider> screenHandler = be -> new DefaultContainerProvider("Machine Infuser")
      .containerSupplier(DefaultContainerProvider.container(MachineInfuserModule.CONTAINER_MACHINE_INFUSER, CONTAINER_FACTORY, be))
      .itemHandler(() -> be.items)
      .energyHandler(() -> be.energyStorage)
      .setupSync(be);
   private final DefaultInfusable infusable = new DefaultInfusable(this);
   @Cap(type = CapType.INFUSABLE)
   private static final Function<MachineInfuserTileEntity, IInfusable> INFUSABLE_HANDLER = be -> be.infusable;

   public MachineInfuserTileEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)MachineInfuserModule.MACHINE_INFUSER.be().get(), pos, state);
   }

   public static BaseBlock createBlock() {
      return new BaseBlock(
         new BlockBuilder()
            .tileEntitySupplier(MachineInfuserTileEntity::new)
            .infusable()
            .manualEntry(ManualHelper.create("rftoolsbase:machines/infusing"))
            .info(new InfoLine[]{TooltipBuilder.key("message.rftoolsbase.shiftmessage")})
            .infoShift(new InfoLine[]{TooltipBuilder.header(), TooltipBuilder.gold()})
      );
   }

   public void tickServer() {
      InfuserData data = (InfuserData)this.getData(MachineInfuserModule.INFUSER_DATA);
      int infusing = data.infusing();
      if (infusing > 0) {
         if (--infusing == 0) {
            ItemStack outputStack = this.items.getStackInSlot(1);
            this.finishInfusing(outputStack);
         }

         this.setData(MachineInfuserModule.INFUSER_DATA, new InfuserData(infusing));
      } else {
         ItemStack inputStack = this.items.getStackInSlot(0);
         ItemStack outputStack = this.items.getStackInSlot(1);
         if (isShard(inputStack) && isInfusable(outputStack)) {
            this.startInfusing();
         }
      }
   }

   protected void loadAdditional(ValueInput input) {
      super.loadAdditional(input);
      this.energyStorage.load(input, "energy");
      this.items.load(input, "items");
      this.infusable.load(input, "infusable");
   }

   protected void saveAdditional(ValueOutput output) {
      super.saveAdditional(output);
      this.energyStorage.save(output, "energy");
      this.items.save(output, "items");
      this.infusable.save(output, "infusable");
   }

   protected void applyImplicitComponents(DataComponentGetter input) {
      super.applyImplicitComponents(input);
      this.energyStorage.applyImplicitComponents((ItemEnergy)input.get((DataComponentType)Registration.ITEM_ENERGY.get()));
      this.items.applyImplicitComponents((ItemInventory)input.get((DataComponentType)Registration.ITEM_INVENTORY.get()));
      this.infusable.applyImplicitComponents((ItemInfusable)input.get((DataComponentType)Registration.ITEM_INFUSABLE.get()));
      InfuserData data = (InfuserData)input.get(MachineInfuserModule.ITEM_INFUSER_DATA);
      if (data != null) {
         this.setData(MachineInfuserModule.INFUSER_DATA, data);
      }
   }

   protected void collectImplicitComponents(Builder builder) {
      super.collectImplicitComponents(builder);
      this.energyStorage.collectImplicitComponents(builder);
      this.items.collectImplicitComponents(builder);
      this.infusable.collectImplicitComponents(builder);
      InfuserData data = (InfuserData)this.getData(MachineInfuserModule.INFUSER_DATA);
      builder.set(MachineInfuserModule.ITEM_INFUSER_DATA, data);
   }

   private static boolean isShard(ItemStack stack) {
      return TagTools.hasTag(stack.getItem(), VariousModule.SHARDS_TAG);
   }

   private static boolean isInfusable(ItemStack stack) {
      return getStackIfInfusable(stack).map(s -> BaseBlock.getInfused(s) < (Integer)MachineInfuserConfiguration.MAX_INFUSE.get()).orElse(false);
   }

   @Nonnull
   private static Optional<ItemStack> getStackIfInfusable(ItemStack stack) {
      if (stack.isEmpty()) {
         return Optional.empty();
      } else {
         Item item = stack.getItem();
         if (!(item instanceof BlockItem)) {
            return Optional.empty();
         } else {
            Block block = ((BlockItem)item).getBlock();
            return block instanceof BaseBlock && ((BaseBlock)block).isInfusable() ? Optional.of(stack) : Optional.empty();
         }
      }
   }

   private void finishInfusing(ItemStack stack) {
      getStackIfInfusable(stack).ifPresent(s -> {
         int oldInfused = BaseBlock.getInfused(s);
         int maxInfuse = (Integer)MachineInfuserConfiguration.MAX_INFUSE.get();
         if (oldInfused < maxInfuse) {
            ItemStack updated = s.copy();
            BaseBlock.setInfused(updated, Math.min(maxInfuse, oldInfused + 1));
            this.items.setStackInSlot(1, updated);
            this.markDirtyClient();
         }
      });
   }

   private void startInfusing() {
      ItemStack machine = this.items.getStackInSlot(1);
      if (isInfusable(machine)) {
         int defaultCost = (Integer)MachineInfuserConfiguration.RFPERTICK.get();
         int rf = (int)(defaultCost * (2.0F - this.infusable.getInfusedFactor()) / 2.0F);
         if (this.energyStorage.getEnergy() >= rf) {
            ItemStack consumed = this.items.extractItem(0, 1, false);
            if (!consumed.isEmpty()) {
               this.energyStorage.consumeEnergy(rf);
               this.setData(MachineInfuserModule.INFUSER_DATA, new InfuserData(5));
               this.markDirtyClient();
            }
         }
      }
   }
}
