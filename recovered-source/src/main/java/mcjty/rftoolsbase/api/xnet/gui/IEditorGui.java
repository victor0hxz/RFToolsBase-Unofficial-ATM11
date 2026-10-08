package mcjty.rftoolsbase.api.xnet.gui;

import mcjty.lib.gui.ITranslatableEnum;
import mcjty.rftoolsbase.api.xnet.channels.RSMode;
import net.minecraft.world.item.ItemStack;

public interface IEditorGui {
   boolean isAdvanced();

   IEditorGui move(int var1, int var2);

   IEditorGui move(int var1);

   IEditorGui shift(int var1);

   IEditorGui label(String var1);

   IEditorGui text(String var1, String var2, String var3, int var4);

   IEditorGui integer(String var1, String var2, Integer var3, int var4, Integer var5);

   IEditorGui integer(String var1, String var2, Integer var3, int var4, int var5, int var6);

   IEditorGui integer(String var1, String var2, Integer var3, int var4);

   IEditorGui real(String var1, String var2, Double var3, int var4);

   IEditorGui toggle(String var1, String var2, boolean var3);

   IEditorGui toggleText(String var1, String var2, String var3, boolean var4);

   IEditorGui colors(String var1, String var2, Integer var3, Integer... var4);

   IEditorGui choices(String var1, String var2, String var3, String... var4);

   <T extends Enum<T>> IEditorGui choices(String var1, String var2, T var3, T... var4);

   IEditorGui translatableChoices(String var1, ITranslatableEnum<?> var2, ITranslatableEnum<?>... var3);

   IEditorGui redstoneMode(String var1, RSMode var2);

   IEditorGui ghostSlot(String var1, ItemStack var2);

   IEditorGui nl();
}
