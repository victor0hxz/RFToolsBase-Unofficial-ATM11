package mcjty.rftoolsbase.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public class KeyBindings {
   private static final Category RFT_CATEGORY = Category.register(Identifier.fromNamespaceAndPath("rftoolsbase", "rftools"));
   public static KeyMapping porterNextDestination;
   public static KeyMapping porterPrevDestination;

   public static void init(RegisterKeyMappingsEvent event) {
      porterNextDestination = new KeyMapping(
         "key.nextDestination", KeyConflictContext.IN_GAME, InputConstants.getKey("key.keyboard.right.bracket"), RFT_CATEGORY
      );
      event.register(porterNextDestination);
      porterPrevDestination = new KeyMapping(
         "key.prevDestination", KeyConflictContext.IN_GAME, InputConstants.getKey("key.keyboard.left.bracket"), RFT_CATEGORY
      );
      event.register(porterPrevDestination);
   }
}
