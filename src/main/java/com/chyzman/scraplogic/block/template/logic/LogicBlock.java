package com.chyzman.scraplogic.block.template.logic;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.enums.Orientation;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public abstract class LogicBlock extends BlockWithEntity {
    public static final EnumProperty<Orientation> ORIENTATION = Properties.ORIENTATION;

    protected LogicBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(ORIENTATION, Orientation.NORTH_UP)
        );
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getPlayerLookDirection().getOpposite();
        Direction direction2 = Direction.UP;
        if (direction.getAxis() == Direction.Axis.Y) {
            direction2 = ctx.getHorizontalPlayerFacing().getOpposite();
            if (direction == Direction.UP) direction2 = direction2.getOpposite();
        }
        return this.getDefaultState().with(ORIENTATION, Orientation.byDirections(direction, direction2));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ORIENTATION);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
