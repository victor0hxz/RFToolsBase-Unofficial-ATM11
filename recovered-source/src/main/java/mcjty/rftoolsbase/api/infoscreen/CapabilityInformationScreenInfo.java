package mcjty.rftoolsbase.api.infoscreen;

import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

public class CapabilityInformationScreenInfo {
   public static final BlockCapability<IInformationScreenInfo, @Nullable Direction> INFORMATION_SCREEN_INFO_CAPABILITY = BlockCapability.createSided(
      Identifier.fromNamespaceAndPath("rftoolsbase", "information_screen"), IInformationScreenInfo.class
   );
}
