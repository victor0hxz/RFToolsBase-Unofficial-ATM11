package mcjty.rftoolsbase.api.screens;

import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface IModuleProvider {
   @Nullable
   Codec<? extends IScreenModule<?, ?>> codec();

   @Nullable
   StreamCodec<RegistryFriendlyByteBuf, ? extends IScreenModule<?, ?>> streamCodec();

   @Nullable
   DataComponentType<? extends IScreenModule<?, ?>> componentType();

   IScreenModule<?, ?> createServerScreenModule();

   IClientScreenModule<?> createClientScreenModule();

   String getModuleName();

   void createGui(IModuleGuiBuilder var1);
}
