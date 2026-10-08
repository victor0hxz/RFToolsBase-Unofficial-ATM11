package mcjty.rftoolsbase.setup;

import mcjty.lib.McJtyLib;
import mcjty.lib.setup.DefaultModSetup;
import mcjty.rftoolsbase.tools.TickOrderHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ModSetup extends DefaultModSetup {
   public void init(FMLCommonSetupEvent e) {
      super.init(e);
      e.enqueueWork(() -> {
         CommandHandler.registerCommands();
         McJtyLib.registerListCommandInfo("getHudLog", String.class, buf -> buf.readUtf(32767), FriendlyByteBuf::writeUtf);
      });
      NeoForge.EVENT_BUS.addListener(event -> {
         if (!event.getLevel().isClientSide()) {
            TickOrderHandler.postWorldTick(event.getLevel().dimension());
         }
      });
   }

   protected void setupModCompat() {
   }
}
