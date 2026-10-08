package mcjty.rftoolsbase.modules.filter.items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import mcjty.lib.varia.ItemStackList;
import mcjty.lib.varia.TagTools;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import mcjty.rftoolsbase.modules.filter.data.FilterModuleData;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FilterModuleInventory {
   private final ItemStackList stacks = ItemStackList.create();
   private final Set<TagKey<Item>> tags = new HashSet<>();
   private final Supplier<ItemStack> filterGetter;

   public FilterModuleInventory(Player player) {
      this.filterGetter = () -> player.getItemInHand(InteractionHand.MAIN_HAND);
      ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
      this.convertFromItem(stack);
   }

   public FilterModuleInventory(ItemStack filterItem) {
      this.filterGetter = () -> filterItem;
      this.convertFromItem(filterItem);
   }

   private void convertFromItem(ItemStack stack) {
      this.stacks.clear();
      this.tags.clear();
      if (stack.is((Item)FilterModule.FILTER_MODULE.get())) {
         FilterModuleData data = (FilterModuleData)stack.getOrDefault(FilterModule.ITEM_FILTERMODULE_DATA, FilterModuleData.EMPTY);
         this.stacks.addAll(data.stacks());
         data.tags().stream().map(TagTools::createItemTagKey).forEach(this.tags::add);
      }
   }

   public void addStack(ItemStack stack) {
      ItemStack toPlace = stack.copy();
      toPlace.setCount(1);

      for (ItemStack s : this.stacks) {
         if (ItemStack.isSameItemSameComponents(s, toPlace)) {
            return;
         }
      }

      this.stacks.add(toPlace);
   }

   public void removeStack(ItemStack stack) {
      for (int i = 0; i < this.stacks.size(); i++) {
         if (ItemStack.isSameItemSameComponents(stack, (ItemStack)this.stacks.get(i))) {
            this.stacks.remove(i);
         }
      }
   }

   public void removeStack(int idx) {
      this.stacks.remove(idx);
   }

   public void removeTag(TagKey<Item> id) {
      this.tags.remove(id);
   }

   public void addTag(TagKey<Item> id) {
      this.tags.add(id);
   }

   public List<ItemStack> getStacks() {
      return this.stacks;
   }

   public Set<TagKey<Item>> getTags() {
      return this.tags;
   }

   public void markDirty() {
      ItemStack heldItem = this.filterGetter.get();
      if (!heldItem.isEmpty()) {
         FilterModuleData data = (FilterModuleData)heldItem.getOrDefault(FilterModule.ITEM_FILTERMODULE_DATA, FilterModuleData.EMPTY);
         FilterModuleData newdata = new FilterModuleData(
            this.stacks, this.tags.stream().<Identifier>map(TagKey::location).toList(), data.blacklist(), data.damage(), data.components(), data.mod()
         );
         heldItem.set(FilterModule.ITEM_FILTERMODULE_DATA, newdata);
      }
   }
}
