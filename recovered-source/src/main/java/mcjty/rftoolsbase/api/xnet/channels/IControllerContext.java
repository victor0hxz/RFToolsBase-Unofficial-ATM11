package mcjty.rftoolsbase.api.xnet.channels;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mcjty.rftoolsbase.api.xnet.keys.ConsumerId;
import mcjty.rftoolsbase.api.xnet.keys.NetworkId;
import mcjty.rftoolsbase.api.xnet.keys.SidedConsumer;
import mcjty.rftoolsbase.api.xnet.keys.SidedPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IControllerContext {
   Level getControllerWorld();

   NetworkId getNetworkId();

   @Nullable
   BlockPos findConsumerPosition(@Nonnull ConsumerId var1);

   @Nonnull
   Map<SidedConsumer, IConnectorSettings> getConnectors(int var1);

   @Nonnull
   Map<SidedConsumer, IConnectorSettings> getRoutedConnectors(int var1);

   boolean matchColor(int var1);

   boolean checkAndConsumeRF(int var1);

   @Nonnull
   Predicate<ItemStack> getIndexedFilter(int var1);

   List<SidedPos> getConnectedBlockPositions();
}
