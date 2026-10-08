package mcjty.rftoolsbase.modules.informationscreen.network;

import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.SafeClientTools;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketMonitorLogReady(BlockPos pos, TypedMap data) implements CustomPacketPayload {
   public static final Identifier ID = Identifier.fromNamespaceAndPath("rftoolsbase", "monitorlogready");
   public static final Type<PacketMonitorLogReady> TYPE = new Type(ID);
   public static final StreamCodec<RegistryFriendlyByteBuf, PacketMonitorLogReady> CODEC = StreamCodec.composite(
      BlockPos.STREAM_CODEC, PacketMonitorLogReady::pos, TypedMap.STREAM_CODEC, PacketMonitorLogReady::data, PacketMonitorLogReady::new
   );

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public static PacketMonitorLogReady create(BlockPos pos, TypedMap data) {
      return new PacketMonitorLogReady(pos, data);
   }

   public void handle(IPayloadContext ctx) {
      ctx.enqueueWork(() -> {
         if (SafeClientTools.getClientWorld().getBlockEntity(this.pos) instanceof InformationScreenTileEntity info) {
            info.setClientData(this.data);
         }
      });
   }
}
