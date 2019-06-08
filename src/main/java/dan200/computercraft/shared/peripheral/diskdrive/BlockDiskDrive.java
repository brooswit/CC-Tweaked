/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2019. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.computercraft.shared.peripheral.diskdrive;

import dan200.computercraft.shared.common.BlockGeneric;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockDiskDrive extends BlockGeneric
{
    static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    static final EnumProperty<DiskDriveState> STATE = EnumProperty.create( "state", DiskDriveState.class );

    public BlockDiskDrive( Properties settings )
    {
        super( settings, TileDiskDrive.FACTORY );
        setDefaultState( getStateContainer().getBaseState()
            .with( FACING, Direction.NORTH )
            .with( STATE, DiskDriveState.EMPTY ) );
    }


    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> properties )
    {
        properties.add( FACING, STATE );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext placement )
    {
        return getDefaultState().with( FACING, placement.getPlacementHorizontalFacing().getOpposite() );
    }

    @Override
    public void harvestBlock( @Nonnull World world, PlayerEntity player, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable TileEntity te, ItemStack stack )
    {
        if( te instanceof INameable && ((INameable) te).hasCustomName() )
        {
            player.addStat( Stats.BLOCK_MINED.get( this ) );
            player.addExhaustion( 0.005F );

            ItemStack result = new ItemStack( this );
            result.setDisplayName( ((INameable) te).getCustomName() );
            spawnAsEntity( world, pos, result );
        }
        else
        {
            super.harvestBlock( world, player, pos, state, te, stack );
        }
    }

    @Override
    public void onBlockPlacedBy( World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack )
    {
        if( stack.hasDisplayName() )
        {
            TileEntity tileentity = world.getTileEntity( pos );
            if( tileentity instanceof TileDiskDrive ) ((TileDiskDrive) tileentity).customName = stack.getDisplayName();
        }
    }
}
