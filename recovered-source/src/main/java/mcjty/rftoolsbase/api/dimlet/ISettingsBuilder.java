package mcjty.rftoolsbase.api.dimlet;

public interface ISettingsBuilder {
   ISettingsBuilder rarity(int var1);

   ISettingsBuilder createCost(int var1);

   ISettingsBuilder maintainCost(int var1);

   ISettingsBuilder tickCost(int var1);

   ISettingsBuilder worldgen(boolean var1);

   ISettingsBuilder dimlet(boolean var1);
}
