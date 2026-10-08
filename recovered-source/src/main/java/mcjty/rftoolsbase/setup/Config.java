package mcjty.rftoolsbase.setup;

import mcjty.lib.modules.Modules;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;

public class Config {
   public static final Builder SERVER_BUILDER = new Builder();
   public static final Builder COMMON_BUILDER = new Builder();
   public static ModConfigSpec SERVER_CONFIG;
   public static ModConfigSpec COMMON_CONFIG;

   public static void register(ModContainer mod, IEventBus bus, Modules modules) {
      modules.initConfig(bus);
      SERVER_CONFIG = SERVER_BUILDER.build();
      COMMON_CONFIG = COMMON_BUILDER.build();
      mod.registerConfig(Type.SERVER, SERVER_CONFIG);
      mod.registerConfig(Type.COMMON, COMMON_CONFIG);
   }
}
