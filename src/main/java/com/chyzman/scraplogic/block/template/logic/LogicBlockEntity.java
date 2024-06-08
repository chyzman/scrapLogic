package com.chyzman.scraplogic.block.template.logic;

import com.chyzman.scraplogic.block.template.SyncedBlockEntity;
import com.chyzman.scraplogic.block.template.TickedBlockEntity;
import net.fabricmc.fabric.api.blockview.v2.RenderDataBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public abstract class LogicBlockEntity<T> extends SyncedBlockEntity implements RenderDataBlockEntity, TickedBlockEntity {
    protected T value;
    private long lastTick = -1;

    public LogicBlockEntity(BlockEntityType<? extends SyncedBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract LogicBlockType<T> valueType();

    public abstract boolean canAcceptConnection(LogicBlockEntity<?> supplier);

    public abstract boolean canSupplyConnection(LogicBlockEntity<?> target);

    public T value() {
        return value;
    }

    //region GETTERS AND SETTERS

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.value = nbt.get(valueType().endec().keyed("value", valueType().defaultValue()));
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.put(valueType().endec().keyed("value", valueType().defaultValue()), value);
    }

    //endregion

    public boolean shouldTick() {
        if (this.lastTick == world.getTime()) return false;
        this.lastTick = world.getTime();
        return true;
    }
}
