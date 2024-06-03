package com.chyzman.scraplogic.block.template;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public abstract class LogicConnectableBlock extends SyncedBlockEntity {
    public static List<BlockPos> parents = new ArrayList<>();

    public LogicConnectableBlock(BlockEntityType<? extends SyncedBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
