package mcjty.rftoolsbase.tools;

import java.util.function.Consumer;
import javax.annotation.Nonnull;
import mcjty.lib.builder.InfoLine;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.api.screens.IModuleProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.neoforge.common.util.Lazy;

public abstract class GenericModuleItem extends Item implements IModuleProvider, ITooltipSettings {
   private final Lazy<TooltipBuilder> tooltipBuilder = Lazy.of(
      () -> new TooltipBuilder()
         .info(new InfoLine[]{TooltipBuilder.key("message.rftoolsbase.shiftmessage")})
         .infoShift(
            new InfoLine[]{
               TooltipBuilder.header(),
               TooltipBuilder.gold(this::hasGoldMessage),
               TooltipBuilder.parameter("uses", this::getUsesString),
               TooltipBuilder.parameter("info", this::getInfoString)
            }
         )
   );

   protected boolean hasGoldMessage(ItemStack stack) {
      return false;
   }

   protected abstract int getUses(ItemStack var1);

   protected String getInfoString(ItemStack stack) {
      return null;
   }

   private String getUsesString(ItemStack stack) {
      return this.getUses(stack) + " RF/tick";
   }

   public GenericModuleItem(Properties properties) {
      super(properties);
   }

   public boolean isPlusModule() {
      return false;
   }

   public void appendHoverText(
      @Nonnull ItemStack itemStack, TooltipContext context, TooltipDisplay tooltipDisplay, @Nonnull Consumer<Component> list, @Nonnull TooltipFlag flag
   ) {
      super.appendHoverText(itemStack, context, tooltipDisplay, list, flag);
      ((TooltipBuilder)this.tooltipBuilder.get()).makeTooltip(Tools.getId(this), itemStack, list, flag);
   }
}
