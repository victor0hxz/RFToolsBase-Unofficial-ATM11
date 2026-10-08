package mcjty.rftoolsbase.api.control.machines;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mcjty.rftoolsbase.api.control.code.ICompiledOpcode;
import mcjty.rftoolsbase.api.control.code.IOpcodeRunnable;
import mcjty.rftoolsbase.api.control.parameters.BlockSide;
import mcjty.rftoolsbase.api.control.parameters.IParameter;
import mcjty.rftoolsbase.api.control.parameters.Inventory;
import mcjty.rftoolsbase.api.control.parameters.Tuple;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

public interface IProcessor {
   @Nullable
   <T> T evaluateParameter(ICompiledOpcode var1, IProgram var2, int var3);

   @Nonnull
   <T> T evaluateParameterNonNull(ICompiledOpcode var1, IProgram var2, int var3);

   ItemStack evaluateItemParameter(ICompiledOpcode var1, IProgram var2, int var3);

   @Nonnull
   ItemStack evaluateItemParameterNonNull(ICompiledOpcode var1, IProgram var2, int var3);

   @Nullable
   FluidStack evaluateFluidParameter(ICompiledOpcode var1, IProgram var2, int var3);

   @Nonnull
   FluidStack evaluateFluidParameterNonNull(ICompiledOpcode var1, IProgram var2, int var3);

   @Nullable
   BlockSide evaluateSideParameter(ICompiledOpcode var1, IProgram var2, int var3);

   @Nonnull
   BlockSide evaluateSideParameterNonNull(ICompiledOpcode var1, IProgram var2, int var3);

   @Nullable
   Inventory evaluateInventoryParameter(ICompiledOpcode var1, IProgram var2, int var3);

   @Nonnull
   Inventory evaluateInventoryParameterNonNull(ICompiledOpcode var1, IProgram var2, int var3);

   @Nullable
   Tuple evaluateTupleParameter(ICompiledOpcode var1, IProgram var2, int var3);

   @Nonnull
   Tuple evaluateTupleParameterNonNull(ICompiledOpcode var1, IProgram var2, int var3);

   @Nullable
   List<IParameter> evaluateVectorParameter(ICompiledOpcode var1, IProgram var2, int var3);

   @Nonnull
   List<IParameter> evaluateVectorParameterNonNull(ICompiledOpcode var1, IProgram var2, int var3);

   int evaluateIntParameter(ICompiledOpcode var1, IProgram var2, int var3);

   long evaluateLngParameter(ICompiledOpcode var1, IProgram var2, int var3);

   @Nullable
   Integer evaluateIntegerParameter(ICompiledOpcode var1, IProgram var2, int var3);

   @Nullable
   Long evaluateLongParameter(ICompiledOpcode var1, IProgram var2, int var3);

   @Nullable
   Number evaluateNumberParameter(ICompiledOpcode var1, IProgram var2, int var3);

   @Nullable
   String evaluateStringParameter(ICompiledOpcode var1, IProgram var2, int var3);

   @Nonnull
   String evaluateStringParameterNonNull(ICompiledOpcode var1, IProgram var2, int var3);

   boolean evaluateBoolParameter(ICompiledOpcode var1, IProgram var2, int var3);

   void setPowerOut(@Nonnull BlockSide var1, int var2);

   int readRedstoneIn(@Nonnull BlockSide var1);

   @Nullable
   BlockEntity getTileEntityAt(@Nullable BlockSide var1);

   @Nullable
   BlockPos getPositionAt(@Nullable BlockSide var1);

   @Nullable
   IFluidHandler getFluidHandlerAt(@Nonnull Inventory var1);

   @Nullable
   IItemHandler getItemHandlerAt(@Nonnull Inventory var1);

   void log(String var1);

   ItemStack getItemInternal(IProgram var1, int var2);

   void setVariable(IProgram var1, int var2);

   IParameter getVariable(IProgram var1, int var2);

   int getEnergy(Inventory var1);

   int getMaxEnergy(Inventory var1);

   long getEnergyLong(Inventory var1);

   long getMaxEnergyLong(Inventory var1);

   IOpcodeRunnable.OpcodeResult placeLock(String var1);

   void releaseLock(String var1);

   boolean testLock(String var1);

   boolean requestCraft(@Nonnull Ingredient var1, @Nullable Inventory var2);

   int getLiquid(@Nonnull Inventory var1);

   int getMaxLiquid(@Nonnull Inventory var1);

   int signal(String var1);

   int signal(Tuple var1);

   ItemStack getCraftResult(IProgram var1);

   ItemStack findCraftingCard(IProgram var1, Inventory var2, ItemStack var3);

   void sendMessage(IProgram var1, int var2, String var3, @Nullable Integer var4);

   void gfxDrawBox(IProgram var1, String var2, @Nonnull Tuple var3, @Nonnull Tuple var4, int var5);

   @Deprecated
   void gfxDrawBox(IProgram var1, String var2, int var3, int var4, int var5, int var6, int var7);

   void gfxDrawLine(IProgram var1, String var2, @Nonnull Tuple var3, @Nonnull Tuple var4, int var5);

   @Deprecated
   void gfxDrawLine(IProgram var1, String var2, int var3, int var4, int var5, int var6, int var7);

   void gfxDrawText(IProgram var1, String var2, @Nonnull Tuple var3, String var4, int var5);

   @Deprecated
   void gfxDrawText(IProgram var1, String var2, int var3, int var4, String var5, int var6);

   void gfxClear(IProgram var1, @Nullable String var2);
}
