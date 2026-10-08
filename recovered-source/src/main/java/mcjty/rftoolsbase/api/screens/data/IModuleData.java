package mcjty.rftoolsbase.api.screens.data;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface IModuleData {
   String getId();

   void writeToBuf(RegistryFriendlyByteBuf var1);
}
