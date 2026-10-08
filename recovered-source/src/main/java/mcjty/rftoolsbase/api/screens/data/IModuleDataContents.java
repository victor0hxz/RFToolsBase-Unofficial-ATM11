package mcjty.rftoolsbase.api.screens.data;

public interface IModuleDataContents extends IModuleData {
   long getContents();

   long getMaxContents();

   long getLastPerTick();
}
