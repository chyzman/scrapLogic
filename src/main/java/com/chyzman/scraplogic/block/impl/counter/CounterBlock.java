package com.chyzman.scraplogic.block.impl.counter;

import com.chyzman.scraplogic.block.template.TickedBlockEntity;
import com.chyzman.scraplogic.block.template.logic.LogicBlock;
import com.chyzman.scraplogic.registry.ScrapLogicBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CounterBlock extends LogicBlock {

    public CounterBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CounterBlockEntity(pos, state);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ScrapLogicBlocks.Entities.COUNTER_BLOCK_ENTITY, TickedBlockEntity.ticker());
    }
}
