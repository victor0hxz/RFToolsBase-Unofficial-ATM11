package mcjty.rftoolsbase.api.screens;

import java.util.List;
import net.minecraft.world.level.Level;

public interface ITooltipInfo {
   List<String> getInfo(Level var1, int var2, int var3);
}
