package mcjty.rftoolsbase.modules.crafting.network;

import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketUpdateNBTItemCard(TypedMap args) implements CustomPacketPayload {
   public static final Identifier ID = Identifier.fromNamespaceAndPath("rftoolsbase", "update_nbt_item_card");
   public static final Type<PacketUpdateNBTItemCard> TYPE = new Type(ID);
   public static final StreamCodec<RegistryFriendlyByteBuf, PacketUpdateNBTItemCard> CODEC = StreamCodec.composite(
      TypedMap.STREAM_CODEC, PacketUpdateNBTItemCard::args, PacketUpdateNBTItemCard::new
   );

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public static PacketUpdateNBTItemCard create(TypedMap arguments) {
      return new PacketUpdateNBTItemCard(arguments);
   }

   protected boolean isValidItem(ItemStack itemStack) {
      return itemStack.getItem() instanceof CraftingCardItem;
   }

   public void handle(IPayloadContext ctx) {
      ctx.enqueueWork(() -> {
         Player player = ctx.player();
         ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
         if (!heldItem.isEmpty()) {
            if (this.isValidItem(heldItem)) {
               ;
            }
         }
      });
   }
}
