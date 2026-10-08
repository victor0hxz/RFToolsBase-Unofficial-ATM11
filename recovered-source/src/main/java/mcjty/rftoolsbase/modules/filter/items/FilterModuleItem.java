package mcjty.rftoolsbase.modules.filter.items;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import mcjty.lib.builder.InfoLine;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.gui.ManualEntry;
import mcjty.lib.tooltips.ITooltipExtras;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.ComponentFactory;
import mcjty.lib.varia.InventoryTools;
import mcjty.lib.varia.TagTools;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import mcjty.rftoolsbase.modules.filter.FilterModuleCache;
import mcjty.rftoolsbase.modules.filter.data.FilterModuleData;
import mcjty.rftoolsbase.tools.ManualHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import org.apache.commons.lang3.tuple.Pair;

public class FilterModuleItem extends Item implements ITooltipSettings, ITooltipExtras {
   public static final ManualEntry MANUAL = ManualHelper.create("rftoolsbase:tools/filtermodule");
   private final Lazy<TooltipBuilder> tooltipBuilder = Lazy.of(
      () -> new TooltipBuilder()
         .info(new InfoLine[]{TooltipBuilder.key("message.rftoolsbase.shiftmessage")})
         .infoShift(new InfoLine[]{TooltipBuilder.header(), TooltipBuilder.gold(), TooltipBuilder.parameter("info", stack -> {
            FilterModuleData data = (FilterModuleData)stack.getOrDefault(FilterModule.ITEM_FILTERMODULE_DATA, FilterModuleData.EMPTY);
            String modeLine = "Mode " + (data.blacklist() ? "blacklist" : "whitelist");
            if (data.damage()) {
               modeLine = modeLine + ", Damage";
            }

            if (data.components()) {
               modeLine = modeLine + ", Comp";
            }

            if (data.mod()) {
               modeLine = modeLine + ", Mod";
            }

            return modeLine;
         })})
   );

   public FilterModuleItem() {
      super(RFToolsBase.setup.defaultProperties().stacksTo(1));
   }

   public ManualEntry getManualEntry() {
      return MANUAL;
   }

   public void appendHoverText(
      @Nonnull ItemStack itemStack, TooltipContext context, TooltipDisplay tooltipDisplay, @Nonnull Consumer<Component> list, @Nonnull TooltipFlag flagIn
   ) {
      super.appendHoverText(itemStack, context, tooltipDisplay, list, flagIn);
      ((TooltipBuilder)this.tooltipBuilder.get()).makeTooltip(Tools.getId(this), itemStack, list, flagIn);
   }

   @Nonnull
   public InteractionResult useOn(UseOnContext context) {
      Player player = context.getPlayer();
      InteractionHand hand = context.getHand();
      Level world = context.getLevel();
      ItemStack stack = player.getItemInHand(hand);
      BlockPos pos = context.getClickedPos();
      if (player.isCrouching()) {
         if (!world.isClientSide()) {
            BlockEntity te = world.getBlockEntity(pos);
            if (InventoryTools.isInventory(te)) {
               FilterModuleInventory inventory = new FilterModuleInventory(stack);
               InventoryTools.getItems(te, s -> true).forEach(inventory::addStack);
               inventory.markDirty();
               player.sendSystemMessage(ComponentFactory.literal(ChatFormatting.GREEN + "Stored inventory contents in filter"));
            } else {
               BlockState state = world.getBlockState(pos);
               ItemStack blockStack = state.getBlock().getCloneItemStack(world, pos, state, false, player);
               if (!blockStack.isEmpty()) {
                  FilterModuleInventory inventory = new FilterModuleInventory(stack);
                  inventory.addStack(blockStack);
                  inventory.markDirty();
                  player.sendSystemMessage(
                     ComponentFactory.literal(ChatFormatting.GREEN + "Added " + blockStack.getHoverName().getString() + " to the filter!")
                  );
               } else {
                  player.sendSystemMessage(
                     ComponentFactory.literal(ChatFormatting.RED + "Could not add " + blockStack.getHoverName().getString() + " to the filter!")
                  );
               }
            }
         }

         return InteractionResult.SUCCESS;
      } else {
         return super.useOn(context);
      }
   }

   @Nonnull
   public InteractionResult use(Level world, Player player, @Nonnull InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!world.isClientSide()) {
         player.openMenu(new MenuProvider() {
            {
               Objects.requireNonNull(FilterModuleItem.this);
            }

            @Nonnull
            public Component getDisplayName() {
               return ComponentFactory.literal("Filter Module");
            }

            public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory, @Nonnull Player playerx) {
               FilterModuleContainer container = new FilterModuleContainer(id, playerx.blockPosition(), playerx);
               container.setupInventories(null, playerInventory);
               return container;
            }
         });
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.SUCCESS;
      }
   }

   public static Predicate<ItemStack> getCache(ItemStack stack) {
      return stack.isEmpty() ? null : new FilterModuleCache(stack);
   }

   public List<Pair<ItemStack, Integer>> getItems(ItemStack stack) {
      FilterModuleInventory inventory = new FilterModuleInventory(stack);
      Set<Item> itemSet = new HashSet<>();

      for (ItemStack s : inventory.getStacks()) {
         itemSet.add(s.getItem());
      }

      for (TagKey<Item> tag : inventory.getTags()) {
         TagTools.getItemsForTag(tag).forEach(i -> itemSet.add((Item)i.value()));
      }

      return itemSet.stream().map(item -> Pair.of(new ItemStack(item), -2)).collect(Collectors.toList());
   }
}
