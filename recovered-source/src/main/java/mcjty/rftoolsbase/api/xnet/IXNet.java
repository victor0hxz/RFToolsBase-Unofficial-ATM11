package mcjty.rftoolsbase.api.xnet;

import javax.annotation.Nonnull;
import mcjty.rftoolsbase.api.xnet.channels.IChannelType;
import mcjty.rftoolsbase.api.xnet.channels.IConnectable;
import mcjty.rftoolsbase.api.xnet.channels.IConsumerProvider;
import mcjty.rftoolsbase.api.xnet.net.IWorldBlob;
import net.minecraft.world.level.Level;

public interface IXNet {
   void registerChannelType(IChannelType var1);

   void registerConnectable(@Nonnull IConnectable var1);

   void registerConsumerProvider(@Nonnull IConsumerProvider var1);

   IWorldBlob getWorldBlob(Level var1);
}
