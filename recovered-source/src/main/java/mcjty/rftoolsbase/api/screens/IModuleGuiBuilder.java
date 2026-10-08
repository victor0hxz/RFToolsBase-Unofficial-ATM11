package mcjty.rftoolsbase.api.screens;

import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IModuleGuiBuilder {
   ItemStack getCurrentModule();

   Level getWorld();

   IModuleGuiBuilder label(String var1);

   IModuleGuiBuilder leftLabel(String var1);

   IModuleGuiBuilder text(BiConsumer<ItemStack, String> var1, Function<ItemStack, String> var2, String... var3);

   IModuleGuiBuilder integer(BiConsumer<ItemStack, Integer> var1, Function<ItemStack, Integer> var2, String... var3);

   IModuleGuiBuilder toggle(BiConsumer<ItemStack, Boolean> var1, Function<ItemStack, Boolean> var2, String var3, String... var4);

   IModuleGuiBuilder toggleNegative(BiConsumer<ItemStack, Boolean> var1, Function<ItemStack, Boolean> var2, String var3, String... var4);

   IModuleGuiBuilder color(BiConsumer<ItemStack, Integer> var1, Function<ItemStack, Integer> var2, String... var3);

   IModuleGuiBuilder choices(BiConsumer<ItemStack, String> var1, Function<ItemStack, String> var2, String var3, String... var4);

   IModuleGuiBuilder choices(BiConsumer<ItemStack, Integer> var1, Function<ItemStack, Integer> var2, IModuleGuiBuilder.Choice... var3);

   IModuleGuiBuilder format(BiConsumer<ItemStack, FormatStyle> var1, Function<ItemStack, FormatStyle> var2);

   IModuleGuiBuilder mode(BiConsumer<ItemStack, BarMode> var1, Function<ItemStack, BarMode> var2, String var3);

   IModuleGuiBuilder block(Function<ItemStack, GlobalPos> var1, Function<ItemStack, String> var2);

   IModuleGuiBuilder ghostStack(BiConsumer<ItemStack, ItemStack> var1, Function<ItemStack, ItemStack> var2);

   IModuleGuiBuilder nl();

   public static class Choice {
      private final String name;
      private final String[] tooltips;

      public Choice(String name, String... tooltips) {
         this.name = name;
         this.tooltips = tooltips;
      }

      public String getName() {
         return this.name;
      }

      public String[] getTooltips() {
         return this.tooltips;
      }
   }
}
