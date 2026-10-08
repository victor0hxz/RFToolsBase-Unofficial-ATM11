package mcjty.rftoolsbase.api.client;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public interface IHudSupport {
   Direction getBlockOrientation();

   boolean isBlockAboveAir();

   List<String> getClientLog();

   long getLastUpdateTime();

   void setLastUpdateTime(long var1);

   BlockPos getHudPos();
}
