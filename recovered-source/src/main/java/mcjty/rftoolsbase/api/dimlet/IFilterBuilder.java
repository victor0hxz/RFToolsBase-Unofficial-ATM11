package mcjty.rftoolsbase.api.dimlet;

public interface IFilterBuilder {
   IFilterBuilder mod(String var1);

   IFilterBuilder name(String var1);

   IFilterBuilder type(String var1);

   IFilterBuilder property(String var1, String var2);

   IFilterBuilder meta(Integer var1);
}
