package mcjty.rftoolsbase.api.xnet.channels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IConnectable {
   IConnectable.ConnectResult canConnect(
      @Nonnull BlockGetter var1, @Nonnull BlockPos var2, @Nonnull BlockPos var3, @Nullable BlockEntity var4, @Nonnull Direction var5
   );

   public static enum ConnectResult {
      NO,
      YES,
      DEFAULT;
   }
}
