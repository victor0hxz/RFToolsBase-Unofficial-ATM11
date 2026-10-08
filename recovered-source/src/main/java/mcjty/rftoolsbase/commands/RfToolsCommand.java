package mcjty.rftoolsbase.commands;

import net.minecraft.world.entity.player.Player;

public interface RfToolsCommand {
   String getHelp();

   int getPermissionLevel();

   boolean isClientSide();

   String getCommand();

   void execute(Player var1, String[] var2);
}
