package mcjty.rftoolsbase.modules.crafting.network;

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

public record PacketItemComponentsToServer(ItemStack stack) implements CustomPacketPayload {
   public static final Identifier ID = Identifier.fromNamespaceAndPath("rftoolsbase", "itemnbt");
   public static final Type<PacketItemComponentsToServer> TYPE = new Type(ID);
   public static final StreamCodec<RegistryFriendlyByteBuf, PacketItemComponentsToServer> CODEC = StreamCodec.composite(
      ItemStack.OPTIONAL_STREAM_CODEC, PacketItemComponentsToServer::stack, PacketItemComponentsToServer::new
   );

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public static PacketItemComponentsToServer create(ItemStack stack) {
      return new PacketItemComponentsToServer(stack);
   }

   public void handle(IPayloadContext ctx) {
      ctx.enqueueWork(() -> {
         Player player = ctx.player();
         ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
         if (!heldItem.isEmpty()) {
            if (heldItem.getItem() instanceof CraftingCardItem) {
               heldItem.applyComponents(this.stack.getComponents());
            }
         }
      });
   }
}
