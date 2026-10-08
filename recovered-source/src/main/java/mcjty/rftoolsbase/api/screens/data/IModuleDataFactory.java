package mcjty.rftoolsbase.api.screens.data;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface IModuleDataFactory<T extends IModuleData> {
   T createData(RegistryFriendlyByteBuf var1);
}
