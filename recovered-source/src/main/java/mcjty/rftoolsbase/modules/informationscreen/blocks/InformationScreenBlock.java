package mcjty.rftoolsbase.modules.informationscreen.blocks;

import javax.annotation.Nonnull;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.blocks.RotationType;
import mcjty.lib.builder.BlockBuilder;
import mcjty.lib.builder.InfoLine;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.varia.OrientationTools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class InformationScreenBlock extends BaseBlock {
   public static final VoxelShape BLOCK_NORTH = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
   public static final VoxelShape BLOCK_SOUTH = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
   public static final VoxelShape BLOCK_WEST = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
   public static final VoxelShape BLOCK_EAST = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);

   public InformationScreenBlock() {
      super(
         new BlockBuilder()
            .info(new InfoLine[]{TooltipBuilder.key("message.rftoolsbase.shiftmessage")})
            .infoShift(new InfoLine[]{TooltipBuilder.header(), TooltipBuilder.gold()})
            .tileEntitySupplier(InformationScreenTileEntity::new)
      );
   }

   @Nonnull
   public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
      Direction side = OrientationTools.getOrientationHoriz(state);

      return switch (side) {
         case NORTH -> BLOCK_SOUTH;
         case EAST -> BLOCK_WEST;
         case WEST -> BLOCK_EAST;
         default -> BLOCK_NORTH;
      };
   }

   @Nonnull
   public InteractionResult useWithoutItem(
      @Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult result
   ) {
      InteractionResult rc = super.useWithoutItem(state, world, pos, player, result);
      if (rc != InteractionResult.SUCCESS) {
         BlockPos offset = pos.relative(OrientationTools.getOrientationHoriz(state).getOpposite());
         result = new BlockHitResult(result.getLocation(), result.getDirection(), offset, result.isInside());
         return world.getBlockState(offset).useWithoutItem(world, player, result);
      } else {
         return rc;
      }
   }

   protected boolean openGui(Level world, int x, int y, int z, Player player) {
      return false;
   }

   protected boolean wrenchUse(Level world, BlockPos pos, Direction side, Player player) {
      if (!world.isClientSide() && world.getBlockEntity(pos) instanceof InformationScreenTileEntity monitor) {
         monitor.toggleMode();
      }

      return true;
   }

   public RotationType getRotationType() {
      return RotationType.HORIZROTATION;
   }

   @Nonnull
   public RenderShape getRenderShape(@Nonnull BlockState state) {
      return RenderShape.MODEL;
   }
}
