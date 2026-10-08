package mcjty.rftoolsbase.api.dimension;

import java.util.UUID;
import net.minecraft.world.level.Level;

public interface IDimensionInformation {
   UUID getOwner();

   long getEnergy();

   long getMaxEnergy(Level var1);

   int getActivityProbes();
}
