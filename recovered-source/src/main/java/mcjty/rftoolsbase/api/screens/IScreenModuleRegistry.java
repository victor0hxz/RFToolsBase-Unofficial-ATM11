package mcjty.rftoolsbase.api.screens;

import mcjty.rftoolsbase.api.screens.data.IModuleDataFactory;

public interface IScreenModuleRegistry {
   void registerModuleDataFactory(String var1, IModuleDataFactory<?> var2);

   IModuleDataFactory<?> getModuleDataFactory(String var1);
}
