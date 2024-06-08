package com.chyzman.scraplogic.block.impl.gate;

import com.chyzman.scraplogic.block.template.logic.LogicBlock;
import com.chyzman.scraplogic.block.template.logic.LogicBlockEntity;
import com.chyzman.scraplogic.block.template.logic.LogicBlockType;
import com.chyzman.scraplogic.block.template.logic.LogicGateMode;
import com.chyzman.scraplogic.registry.ScrapLogicBlocks;
import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.endec.KeyedEndec;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static com.chyzman.scraplogic.registry.ComponentInit.SCRAP_LOGIC_WORLD;

@SuppressWarnings("unchecked")
public class GateBlockEntity extends LogicBlockEntity<Boolean> {
    private LogicGateMode mode = MODE_KEY.defaultValue();

    //region ENDEC STUFF

    private static final KeyedEndec<LogicGateMode> MODE_KEY = Endec.forEnum(LogicGateMode.class).keyed("mode", LogicGateMode.AND);

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.mode = nbt.get(MODE_KEY);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.put(MODE_KEY, mode);
    }

    //endregion

    public GateBlockEntity(BlockPos pos, BlockState state) {
        super(ScrapLogicBlocks.Entities.LOGIC_GATE_BLOCK_ENTITY, pos, state);
        this.value = LogicBlockType.BOOL.defaultValue();
    }

    public GateBlockEntity(BlockPos pos, BlockState state, boolean value) {
        this(pos, state);
        this.value = value;
    }

    @Override
    public LogicBlockType<Boolean> valueType() {
        return LogicBlockType.BOOL;
    }

    @Override
    public boolean canAcceptConnection(LogicBlockEntity<?> supplier) {
        return true;
    }

    @Override
    public boolean canSupplyConnection(LogicBlockEntity<?> target) {
        return true;
    }

    @Override
    public void tickServer() {
        if (!shouldTick()) return;
        super.tickServer();
        var world = getWorld();
        if (world == null) return;
        var worldComponent = SCRAP_LOGIC_WORLD.get(world);
        this.value = mode.function.apply(worldComponent.getParents(this.pos)
                .stream()
                .map(connection -> {
                    var parent = connection.start();
                    var entity = world.getBlockEntity(parent);
                    if (!(entity instanceof LogicBlockEntity logicBlockEntity)) {
                        var state = world.getBlockState(parent);
                        if (state.getProperties().contains(Properties.POWERED)) {
                            return state.get(Properties.POWERED);
                        } else {
                            var power = false;
                            for (Direction direction : Direction.values()) {
                                power = power || state.getStrongRedstonePower(world, parent, direction) > 0;
                            }
                            return power;
                        }
                    } else {
                        return logicBlockEntity.valueType().isTrueIsh(logicBlockEntity.value());
                    }
                }).toList()
        );
        this.markDirty();
    }

    public LogicGateMode getMode() {
        return mode;
    }
}
