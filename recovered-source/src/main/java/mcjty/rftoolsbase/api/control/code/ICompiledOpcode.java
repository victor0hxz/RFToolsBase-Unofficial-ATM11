package mcjty.rftoolsbase.api.control.code;

import java.util.List;
import javax.annotation.Nonnull;
import mcjty.rftoolsbase.api.control.parameters.IParameter;

public interface ICompiledOpcode {
   @Nonnull
   List<IParameter> getParameters();
}
