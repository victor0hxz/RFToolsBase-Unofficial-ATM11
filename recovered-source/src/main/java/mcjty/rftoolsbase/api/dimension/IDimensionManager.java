package mcjty.rftoolsbase.api.dimension;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public interface IDimensionManager {
   String RFTOOLSDIMENSIONS = "rftoolsdim";
   String GET_DIMENSION_MANAGER = "getDimensionManager";

   IDimensionInformation getDimensionInformation(Level var1, Identifier var2);
}
