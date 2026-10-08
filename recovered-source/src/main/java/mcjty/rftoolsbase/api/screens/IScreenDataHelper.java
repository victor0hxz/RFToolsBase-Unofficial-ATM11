package mcjty.rftoolsbase.api.screens;

import mcjty.rftoolsbase.api.screens.data.IModuleDataBoolean;
import mcjty.rftoolsbase.api.screens.data.IModuleDataContents;
import mcjty.rftoolsbase.api.screens.data.IModuleDataInteger;
import mcjty.rftoolsbase.api.screens.data.IModuleDataString;

public interface IScreenDataHelper {
   IModuleDataInteger createInteger(int var1);

   IModuleDataBoolean createBoolean(boolean var1);

   IModuleDataString createString(String var1);

   IModuleDataContents createContents(long var1, long var3, long var5);
}
