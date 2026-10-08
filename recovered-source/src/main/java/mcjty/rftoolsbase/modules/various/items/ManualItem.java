package mcjty.rftoolsbase.modules.various.items;

import java.util.function.Consumer;
import javax.annotation.Nonnull;
import mcjty.lib.builder.InfoLine;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.compat.patchouli.PatchouliCompatibility;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;

public class ManualItem extends Item implements ITooltipSettings {
   private final Lazy<TooltipBuilder> tooltipBuilder = Lazy.of(
      () -> new TooltipBuilder()
         .info(new InfoLine[]{TooltipBuilder.key("message.rftoolsbase.shiftmessage")})
         .infoShift(new InfoLine[]{TooltipBuilder.header()})
   );

   public ManualItem() {
      super(Registration.createStandardProperties().stacksTo(1));
   }

   @Nonnull
   public InteractionResult use(Level worldIn, @Nonnull Player playerIn, @Nonnull InteractionHand handIn) {
      if (!worldIn.isClientSide()) {
         PatchouliCompatibility.openBookGUI((ServerPlayer)playerIn, Identifier.fromNamespaceAndPath("rftoolsbase", "manual"));
      }

      return super.use(worldIn, playerIn, handIn);
   }

   public void appendHoverText(
      @Nonnull ItemStack itemStack, TooltipContext context, TooltipDisplay tooltipDisplay, @Nonnull Consumer<Component> list, @Nonnull TooltipFlag flags
   ) {
      super.appendHoverText(itemStack, context, tooltipDisplay, list, flags);
      ((TooltipBuilder)this.tooltipBuilder.get()).makeTooltip(Tools.getId(this), itemStack, list, flags);
   }
}
