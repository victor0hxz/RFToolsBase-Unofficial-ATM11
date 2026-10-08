package mcjty.rftoolsbase.api.control.machines;

import javax.annotation.Nullable;
import mcjty.rftoolsbase.api.control.parameters.IParameter;

public interface IProgram {
   void setLastValue(IParameter var1);

   @Nullable
   IParameter getLastValue();

   @Nullable
   String getCraftTicket();

   boolean hasCraftTicket();

   void setDelay(int var1);

   int getDelay();

   void killMe();

   boolean isDead();
}
