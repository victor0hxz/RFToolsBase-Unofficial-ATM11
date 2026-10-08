package mcjty.rftoolsbase.modules.various.items;

import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import mcjty.lib.api.smartwrench.ISmartWrenchSelector;
import mcjty.lib.api.smartwrench.SmartWrench;
import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.builder.InfoLine;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.BlockPosTools;
import mcjty.lib.varia.Logging;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.modules.various.data.WrenchData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.util.Lazy;

public class SmartWrenchItem extends Item implements SmartWrench, ITooltipSettings {
   private final SmartWrenchMode mode;
   private final Lazy<TooltipBuilder> tooltipBuilder = Lazy.of(
      () -> new TooltipBuilder()
         .info(new InfoLine[]{TooltipBuilder.key("message.rftoolsbase.shiftmessage")})
         .infoShift(
            new InfoLine[]{
               TooltipBuilder.header(),
               TooltipBuilder.gold(),
               TooltipBuilder.parameter("info1", stack -> this.getMode().getName()),
               TooltipBuilder.parameter("info2", stack -> getCurrentBlock(stack).<String>map(BlockPosTools::toString).orElse("<not selected>"))
            }
         )
   );

   public SmartWrenchItem(SmartWrenchMode mode) {
      super(RFToolsBase.setup.defaultProperties().stacksTo(1));
      this.mode = mode;
   }

   @Nonnull
   public InteractionResult use(Level world, Player player, @Nonnull InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!world.isClientSide()) {
         SmartWrenchMode mode = getCurrentMode(stack);
         WrenchData data = (WrenchData)stack.get(VariousModule.ITEM_WRENCH_DATA);
         ItemStack newStack;
         if (mode == SmartWrenchMode.MODE_WRENCH) {
            mode = SmartWrenchMode.MODE_SELECT;
            newStack = new ItemStack((ItemLike)VariousModule.SMARTWRENCH_SELECT.get());
         } else {
            mode = SmartWrenchMode.MODE_WRENCH;
            newStack = new ItemStack((ItemLike)VariousModule.SMARTWRENCH.get());
         }

         newStack.set(VariousModule.ITEM_WRENCH_DATA, data);
         player.setItemInHand(hand, newStack);
         Logging.message(player, ChatFormatting.YELLOW + "Smart wrench is now in " + mode.getName() + " mode.");
      }

      return super.use(world, player, hand);
   }

   @Nonnull
   public InteractionResult useOn(UseOnContext context) {
      Level world = context.getLevel();
      if (!world.isClientSide()) {
         Player player = context.getPlayer();
         InteractionHand hand = context.getHand();
         ItemStack stack = context.getItemInHand();
         BlockPos pos = context.getClickedPos();
         if (player != null && player.isShiftKeyDown()) {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (block instanceof BaseBlock
               && state.useWithoutItem(world, player, new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos, context.isInside()))
                  == InteractionResult.SUCCESS) {
               return InteractionResult.SUCCESS;
            }
         }

         SmartWrenchMode mode = getCurrentMode(stack);
         if (mode == SmartWrenchMode.MODE_SELECT) {
            return (InteractionResult)getCurrentBlock(stack).map(b -> {
               if (!b.dimension().equals(world.dimension())) {
                  if (player != null) {
                     Logging.message(player, ChatFormatting.RED + "The selected block is in another dimension!");
                  }

                  return InteractionResult.FAIL;
               } else {
                  if (world.getBlockEntity(b.pos()) instanceof ISmartWrenchSelector smartWrenchSelector) {
                     smartWrenchSelector.selectBlock(player, pos);
                  }

                  return InteractionResult.SUCCESS;
               }
            }).orElse(InteractionResult.SUCCESS);
         }
      }

      return InteractionResult.SUCCESS;
   }

   public void appendHoverText(
      @Nonnull ItemStack itemStack, TooltipContext context, TooltipDisplay tooltipDisplay, @Nonnull Consumer<Component> list, @Nonnull TooltipFlag flags
   ) {
      super.appendHoverText(itemStack, context, tooltipDisplay, list, flags);
      ((TooltipBuilder)this.tooltipBuilder.get()).makeTooltip(Tools.getId(this), itemStack, list, flags);
   }

   public SmartWrenchMode getMode() {
      return this.mode;
   }

   public SmartWrenchMode getMode(ItemStack itemStack) {
      return getCurrentMode(itemStack);
   }

   public static SmartWrenchMode getCurrentMode(ItemStack itemStack) {
      return itemStack.getItem() instanceof SmartWrenchItem ? ((SmartWrenchItem)itemStack.getItem()).getMode() : SmartWrenchMode.MODE_WRENCH;
   }

   public static void setCurrentBlock(ItemStack itemStack, GlobalPos c) {
      if (c == null) {
         itemStack.remove(VariousModule.ITEM_WRENCH_DATA);
      } else {
         itemStack.set(VariousModule.ITEM_WRENCH_DATA, new WrenchData(c));
      }
   }

   @Nonnull
   public static Optional<GlobalPos> getCurrentBlock(ItemStack itemStack) {
      WrenchData data = (WrenchData)itemStack.get(VariousModule.ITEM_WRENCH_DATA);
      return data != null ? Optional.of(data.pos()) : Optional.empty();
   }

   public int getUseDuration(@Nonnull ItemStack stack, LivingEntity entity) {
      return 1;
   }
}
