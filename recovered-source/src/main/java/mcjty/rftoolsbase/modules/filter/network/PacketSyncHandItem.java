package mcjty.rftoolsbase.modules.filter.network;

import mcjty.rftoolsbase.modules.filter.items.FilterModuleItem;
import mcjty.rftoolsbase.modules.tablet.items.TabletItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketSyncHandItem(ItemStack stack) implements CustomPacketPayload {
   public static final Identifier ID = Identifier.fromNamespaceAndPath("rftoolsbase", "synchanditem");
   public static final Type<PacketSyncHandItem> TYPE = new Type(ID);
   public static final StreamCodec<RegistryFriendlyByteBuf, PacketSyncHandItem> CODEC = StreamCodec.composite(
      ItemStack.STREAM_CODEC, PacketSyncHandItem::stack, PacketSyncHandItem::new
   );

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public static PacketSyncHandItem create(Player player) {
      return new PacketSyncHandItem(player.getItemInHand(InteractionHand.MAIN_HAND));
   }

   protected boolean isValidItem(ItemStack itemStack) {
      return itemStack.getItem() instanceof FilterModuleItem || itemStack.getItem() instanceof TabletItem;
   }

   public void handle(IPayloadContext ctx) {
      ctx.enqueueWork(() -> {
         Player player = ctx.player();
         ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
         if (!heldItem.isEmpty()) {
            if (this.isValidItem(heldItem)) {
               if (this.isValidItem(this.stack)) {
                  player.setItemInHand(InteractionHand.MAIN_HAND, this.stack);
               }
            }
         }
      });
   }
}
