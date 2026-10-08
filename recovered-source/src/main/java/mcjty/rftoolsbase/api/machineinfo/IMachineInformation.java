package mcjty.rftoolsbase.api.machineinfo;

public interface IMachineInformation {
   int getTagCount();

   String getTagName(int var1);

   String getTagDescription(int var1);

   String getData(int var1, long var2);
}
