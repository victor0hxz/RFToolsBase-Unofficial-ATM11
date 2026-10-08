package mcjty.rftoolsbase.api.xnet.channels;

import com.mojang.serialization.MapCodec;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;

public interface IChannelType {
   String getID();

   String getName();

   MapCodec<? extends IChannelSettings> getCodec();

   MapCodec<? extends IConnectorSettings> getConnectorCodec();

   StreamCodec<RegistryFriendlyByteBuf, ? extends IChannelSettings> getStreamCodec();

   StreamCodec<RegistryFriendlyByteBuf, ? extends IConnectorSettings> getConnectorStreamCodec();

   boolean supportsBlock(@Nonnull Level var1, @Nonnull BlockPos var2, @Nullable Direction var3);

   @Nonnull
   IConnectorSettings createConnector(@Nonnull Direction var1);

   @Nonnull
   IChannelSettings createChannel();
}
