package mcjty.rftoolsbase.api.dimlet;

public interface IDimletConfigurationManager {
   IFilterBuilder createFilterBuilder();

   ISettingsBuilder createSettingsBuilder();

   void addRule(IFilterBuilder var1, ISettingsBuilder var2);
}
