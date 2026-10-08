package mcjty.rftoolsbase.modules.filter;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import mcjty.lib.varia.ItemStackList;
import mcjty.lib.varia.TagTools;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.modules.filter.data.FilterModuleData;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleInventory;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FilterModuleCache implements Predicate<ItemStack> {
   private final boolean matchDamage;
   private final boolean blacklistMode;
   private final boolean componentMode;
   private final boolean modMode;
   private final ItemStackList stacks;
   private final Set<TagKey<Item>> tags;

   public FilterModuleCache(ItemStack stack) {
      FilterModuleData data = (FilterModuleData)stack.getOrDefault(FilterModule.ITEM_FILTERMODULE_DATA, FilterModuleData.EMPTY);
      this.matchDamage = data.damage();
      this.componentMode = data.components();
      this.modMode = data.mod();
      this.blacklistMode = data.blacklist();
      FilterModuleInventory inventory = new FilterModuleInventory(stack);
      this.tags = new HashSet<>();
      this.stacks = ItemStackList.create();
      this.stacks.addAll(inventory.getStacks());
      this.tags.addAll(inventory.getTags());
   }

   public boolean test(ItemStack stack) {
      if (!stack.isEmpty()) {
         boolean match = false;
         String modName = "";
         if (this.modMode) {
            modName = Tools.getId(stack).getNamespace();
         }

         if (!this.tags.isEmpty()) {
            for (TagKey<Item> tag : TagTools.getTags(stack.getItem())) {
               if (this.tags.contains(tag)) {
                  match = true;
                  break;
               }
            }
         }

         if (!match) {
            match = this.itemMatches(stack, modName);
         }

         return match != this.blacklistMode;
      } else {
         return false;
      }
   }

   private boolean itemMatches(ItemStack stack, String modName) {
      if (this.stacks != null) {
         for (ItemStack itemStack : this.stacks) {
            if ((!this.matchDamage || itemStack.getDamageValue() == stack.getDamageValue())
               && (!this.componentMode || ItemStack.isSameItemSameComponents(itemStack, stack))) {
               if (this.modMode) {
                  if (modName.equals(Tools.getId(itemStack).getNamespace())) {
                     return true;
                  }
               } else if (itemStack.getItem().equals(stack.getItem())) {
                  return true;
               }
            }
         }
      }

      return false;
   }
}
