package mcjty.rftoolsbase.api.xnet.net;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mcjty.rftoolsbase.api.xnet.keys.ConsumerId;
import mcjty.rftoolsbase.api.xnet.keys.NetworkId;
import net.minecraft.core.BlockPos;

public interface IWorldBlob {
   void markNetworkDirty(NetworkId var1);

   @Nonnull
   Set<NetworkId> getNetworksAt(@Nonnull BlockPos var1);

   @Nullable
   BlockPos getProviderPosition(@Nonnull NetworkId var1);

   @Nonnull
   Set<BlockPos> getConsumers(NetworkId var1);

   @Nullable
   BlockPos getConsumerPosition(@Nonnull ConsumerId var1);

   @Nullable
   ConsumerId getConsumerAt(@Nonnull BlockPos var1);
}
