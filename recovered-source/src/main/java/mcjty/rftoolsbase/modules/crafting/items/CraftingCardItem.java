package mcjty.rftoolsbase.modules.crafting.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mcjty.lib.builder.InfoLine;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.crafting.BaseRecipe;
import mcjty.lib.gui.ManualEntry;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.ComponentFactory;
import mcjty.lib.varia.ItemStackList;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import mcjty.rftoolsbase.modules.crafting.data.CraftingCardData;
import mcjty.rftoolsbase.tools.ManualHelper;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;

public class CraftingCardItem extends Item implements ITooltipSettings {
   public static final ManualEntry MANUAL = ManualHelper.create("rftoolsbase:tools/craftingcard");
   private static CraftingInput CRAFTING_INVENTORY = CraftingInput.of(3, 3, createList());
   private final Lazy<TooltipBuilder> tooltipBuilder = Lazy.of(
      () -> new TooltipBuilder().info(new InfoLine[]{TooltipBuilder.header(), TooltipBuilder.parameter("info", stack -> {
         ItemStack result = getResult(stack);
         if (!result.isEmpty()) {
            return result.getCount() > 1 ? result.getHoverName().getString() + "(" + result.getCount() + ")" : result.getHoverName().getString();
         } else {
            return "<empty>";
         }
      })})
   );

   private static List<ItemStack> createList() {
      List<ItemStack> list = new ArrayList<>();

      for (int i = 0; i < 9; i++) {
         list.add(ItemStack.EMPTY);
      }

      return list;
   }

   public ManualEntry getManualEntry() {
      return MANUAL;
   }

   public CraftingCardItem() {
      super(RFToolsBase.setup.defaultProperties().stacksTo(8));
   }

   @Nullable
   private static Recipe<?> findRecipeInternal(Level world, CraftingInput inv, RecipeType<?> type) {
      if (world.getServer() == null) {
         return null;
      } else {
         for (RecipeHolder<?> rh : world.getServer().getRecipeManager().getRecipes()) {
            Recipe<?> r = rh.value();
            if (r != null && type.equals(r.getType()) && recipeMatch(r, inv, world)) {
               return r;
            }
         }

         return null;
      }
   }

   private static <T extends RecipeInput> boolean recipeMatch(Recipe<T> r, T inv, Level world) {
      return r.matches(inv, world);
   }

   @Nullable
   public static Recipe findRecipe(Level world, ItemStack craftingCard, RecipeType<?> type) {
      ItemStackList stacks = getStacksFromItem(craftingCard);
      List<ItemStack> list = new ArrayList<>(9);

      for (int y = 0; y < 3; y++) {
         for (int x = 0; x < 3; x++) {
            int idxCard = y * 5 + x;
            list.add((ItemStack)stacks.get(idxCard));
         }
      }

      CRAFTING_INVENTORY = CraftingInput.of(3, 3, list);
      return findRecipeInternal(world, CRAFTING_INVENTORY, type);
   }

   public static void testRecipe(Level world, ItemStack craftingCard) {
      ItemStackList stacks = getStacksFromItem(craftingCard);
      List<ItemStack> list = new ArrayList<>(9);

      for (int y = 0; y < 3; y++) {
         for (int x = 0; x < 3; x++) {
            int idxCard = y * 5 + x;
            list.add((ItemStack)stacks.get(idxCard));
         }
      }

      CRAFTING_INVENTORY = CraftingInput.of(3, 3, list);
      Recipe recipe = findRecipeInternal(world, CRAFTING_INVENTORY, RecipeType.CRAFTING);
      if (recipe != null) {
         ItemStack stack = BaseRecipe.assemble(recipe, CRAFTING_INVENTORY, world);
         stacks.set(20, stack);
      } else {
         stacks.set(20, ItemStack.EMPTY);
      }

      putStacksInItem(craftingCard, stacks);
   }

   private static CraftingCardData defaultData() {
      ItemStackList stacks = ItemStackList.create(21);
      Collections.fill(stacks, ItemStack.EMPTY);
      return new CraftingCardData(stacks);
   }

   public static ItemStackList getStacksFromItem(ItemStack craftingCard) {
      CraftingCardData data = (CraftingCardData)craftingCard.getOrDefault(CraftingModule.ITEM_CRAFTINGCARD_DATA, defaultData());
      ItemStackList stacks = ItemStackList.create(21);

      for (int i = 0; i < stacks.size(); i++) {
         if (i < data.stacks().size()) {
            stacks.set(i, data.stacks().get(i));
         } else {
            stacks.set(i, ItemStack.EMPTY);
         }
      }

      return stacks;
   }

   public static void putStacksInItem(ItemStack craftingCard, ItemStackList stacks) {
      craftingCard.update(CraftingModule.ITEM_CRAFTINGCARD_DATA, defaultData(), data -> {
         List<ItemStack> list = new ArrayList<>(data.stacks());

         for (int i = 0; i < list.size(); i++) {
            if (i < stacks.size()) {
               list.set(i, (ItemStack)stacks.get(i));
            } else {
               list.add(ItemStack.EMPTY);
            }
         }

         return data.withStacks(list);
      });
   }

   public void appendHoverText(
      @Nonnull ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, @Nonnull Consumer<Component> list, @Nonnull TooltipFlag flagIn
   ) {
      super.appendHoverText(stack, context, tooltipDisplay, list, flagIn);
      ((TooltipBuilder)this.tooltipBuilder.get()).makeTooltip(Tools.getId(this), stack, list, flagIn);
   }

   @Nonnull
   public InteractionResult use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.PASS;
      } else if (!world.isClientSide()) {
         player.openMenu(new MenuProvider() {
            {
               Objects.requireNonNull(CraftingCardItem.this);
            }

            @Nonnull
            public Component getDisplayName() {
               return ComponentFactory.literal("Crafting Card");
            }

            public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory, @Nonnull Player playerx) {
               CraftingCardContainer container = new CraftingCardContainer(id, playerx.blockPosition(), playerx);
               container.setupInventories(null, playerInventory);
               return container;
            }
         });
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.SUCCESS;
      }
   }

   public static ItemStack getResult(ItemStack card) {
      CraftingCardData data = (CraftingCardData)card.getOrDefault(CraftingModule.ITEM_CRAFTINGCARD_DATA, defaultData());
      return data.stacks().get(20);
   }

   private static boolean isInGrid(int index) {
      int x = index % 5;
      int y = index / 5;
      return x <= 2 && y <= 2;
   }

   public static boolean fitsGrid(ItemStack card) {
      CraftingCardData data = (CraftingCardData)card.getOrDefault(CraftingModule.ITEM_CRAFTINGCARD_DATA, defaultData());

      for (int i = 0; i < data.stacks().size(); i++) {
         if (i < 20 && !isInGrid(i)) {
            return false;
         }
      }

      return true;
   }

   public static List<Ingredient> getIngredientsGrid(ItemStack card) {
      CraftingCardData data = (CraftingCardData)card.getOrDefault(CraftingModule.ITEM_CRAFTINGCARD_DATA, defaultData());
      List<Ingredient> stacks = new ArrayList<>();

      for (int i = 0; i < data.stacks().size(); i++) {
         if (i < 20 && isInGrid(i)) {
            stacks.add(Ingredient.of(data.stacks().get(i).getItem()));
         }
      }

      return stacks;
   }

   public static List<ItemStack> getIngredientStacks(ItemStack card) {
      CraftingCardData data = (CraftingCardData)card.getOrDefault(CraftingModule.ITEM_CRAFTINGCARD_DATA, defaultData());
      List<ItemStack> stacks = new ArrayList<>();

      for (int i = 0; i < data.stacks().size(); i++) {
         if (i < 20 && isInGrid(i)) {
            stacks.add(data.stacks().get(i));
         }
      }

      return stacks;
   }

   public static List<Ingredient> getIngredients(ItemStack card) {
      CraftingCardData data = (CraftingCardData)card.getOrDefault(CraftingModule.ITEM_CRAFTINGCARD_DATA, defaultData());
      List<Ingredient> stacks = new ArrayList<>();

      for (int i = 0; i < data.stacks().size(); i++) {
         if (i < 20) {
            stacks.add(Ingredient.of(data.stacks().get(i).getItem()));
         }
      }

      return stacks;
   }
}
