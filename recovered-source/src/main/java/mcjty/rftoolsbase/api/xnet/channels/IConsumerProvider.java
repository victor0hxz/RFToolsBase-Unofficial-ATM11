package mcjty.rftoolsbase.api.xnet.channels;

import java.util.Set;
import javax.annotation.Nonnull;
import mcjty.rftoolsbase.api.xnet.keys.NetworkId;
import mcjty.rftoolsbase.api.xnet.net.IWorldBlob;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IConsumerProvider {
   @Nonnull
   Set<BlockPos> getConsumers(Level var1, IWorldBlob var2, NetworkId var3);
}
