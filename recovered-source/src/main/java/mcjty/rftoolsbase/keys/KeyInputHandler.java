package mcjty.rftoolsbase.keys;

import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.setup.CommandHandler;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent.Key;

public class KeyInputHandler {
   @SubscribeEvent
   public void onKeyInput(Key event) {
      if (KeyBindings.porterNextDestination.consumeClick()) {
         RFToolsBaseMessages.sendToServer("cycleDestination", TypedMap.builder().put(CommandHandler.PARAM_NEXT, true));
      } else if (KeyBindings.porterPrevDestination.consumeClick()) {
         RFToolsBaseMessages.sendToServer("cycleDestination", TypedMap.builder().put(CommandHandler.PARAM_NEXT, false));
      }
   }
}
