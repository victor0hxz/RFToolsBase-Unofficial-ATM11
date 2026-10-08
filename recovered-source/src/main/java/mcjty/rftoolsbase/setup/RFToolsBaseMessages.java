package mcjty.rftoolsbase.setup;

import javax.annotation.Nonnull;
import mcjty.lib.network.Networking;
import mcjty.lib.network.PacketSendServerCommand;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.typed.TypedMap.Builder;
import mcjty.rftoolsbase.modules.crafting.network.PacketItemComponentsToServer;
import mcjty.rftoolsbase.modules.crafting.network.PacketUpdateNBTItemCard;
import mcjty.rftoolsbase.modules.filter.network.PacketSyncHandItem;
import mcjty.rftoolsbase.modules.informationscreen.network.PacketGetMonitorLog;
import mcjty.rftoolsbase.modules.informationscreen.network.PacketMonitorLogReady;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class RFToolsBaseMessages {
   public static void registerMessages(RegisterPayloadHandlersEvent event) {
      PayloadRegistrar registrar = event.registrar("rftoolsbase").versioned("1.0").optional();
      registrar.playToServer(PacketItemComponentsToServer.TYPE, PacketItemComponentsToServer.CODEC, PacketItemComponentsToServer::handle);
      registrar.playToServer(PacketUpdateNBTItemCard.TYPE, PacketUpdateNBTItemCard.CODEC, PacketUpdateNBTItemCard::handle);
      registrar.playToServer(PacketGetMonitorLog.TYPE, PacketGetMonitorLog.CODEC, PacketGetMonitorLog::handle);
      registrar.playToServer(PacketSyncHandItem.TYPE, PacketSyncHandItem.CODEC, PacketSyncHandItem::handle);
      registrar.playToClient(PacketMonitorLogReady.TYPE, PacketMonitorLogReady.CODEC);
   }

   public static void registerClientMessages(RegisterClientPayloadHandlersEvent event) {
      event.register(PacketMonitorLogReady.TYPE, PacketMonitorLogReady::handle);
   }

   public static void sendToServer(String command, @Nonnull Builder argumentBuilder) {
      Networking.sendToServer(new PacketSendServerCommand("rftoolsbase", command, argumentBuilder.build()));
   }

   public static void sendToServer(String command) {
      Networking.sendToServer(new PacketSendServerCommand("rftoolsbase", command, TypedMap.EMPTY));
   }

   public static <T extends CustomPacketPayload> void sendToPlayer(T packet, Player player) {
      PacketDistributor.sendToPlayer((ServerPlayer)player, packet, new CustomPacketPayload[0]);
   }

   public static <T extends CustomPacketPayload> void sendToServer(T packet) {
      ClientPacketDistributor.sendToServer(packet, new CustomPacketPayload[0]);
   }
}
