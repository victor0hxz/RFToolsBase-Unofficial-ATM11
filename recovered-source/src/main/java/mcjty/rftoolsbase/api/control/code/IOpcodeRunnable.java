package mcjty.rftoolsbase.api.control.code;

import mcjty.rftoolsbase.api.control.machines.IProcessor;
import mcjty.rftoolsbase.api.control.machines.IProgram;

public interface IOpcodeRunnable {
   IOpcodeRunnable.OpcodeResult run(IProcessor var1, IProgram var2, ICompiledOpcode var3);

   public static enum OpcodeResult {
      POSITIVE,
      NEGATIVE,
      HOLD;
   }
}
