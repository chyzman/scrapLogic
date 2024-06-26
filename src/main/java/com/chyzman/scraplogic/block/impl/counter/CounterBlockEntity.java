package com.chyzman.scraplogic.block.impl.counter;

import com.chyzman.scraplogic.block.template.logic.LogicBlock;
import com.chyzman.scraplogic.block.template.logic.LogicBlockEntity;
import com.chyzman.scraplogic.block.template.logic.LogicBlockType;
import com.chyzman.scraplogic.registry.ScrapLogicBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import static com.chyzman.scraplogic.registry.ComponentInit.SCRAP_LOGIC_WORLD;

public class CounterBlockEntity extends LogicBlockEntity<BigDecimal> {
    public CounterBlockEntity(BlockPos pos, BlockState state) {
        super(ScrapLogicBlocks.Entities.COUNTER_BLOCK_ENTITY, pos, state);
        this.value = LogicBlockType.NUMBER.defaultValue();
    }

    public CounterBlockEntity(BlockPos pos, BlockState state, BigDecimal value) {
        this(pos, state);
        this.value = value;
    }

    @Override
    public LogicBlockType<BigDecimal> valueType() {
        return LogicBlockType.NUMBER;
    }

    @Override
    public boolean canAcceptConnection(LogicBlockEntity<?> supplier) {
        return true;
    }

    @Override
    public boolean canSupplyConnection(LogicBlockEntity<?> target) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void tickServer() {
        if (!shouldTick()) return;
        super.tickServer();
        var world = getWorld();
        if (world == null) return;
        var worldComponent = SCRAP_LOGIC_WORLD.get(world);
        this.value = this.value.add(worldComponent.getParents(this.pos)
                .stream()
                .map(connection -> {
                    var parent = connection.start();
                    var entity = world.getBlockEntity(parent);
                    if (!(entity instanceof LogicBlockEntity logicBlockEntity)) {
                        var state = world.getBlockState(parent);
                        if (state.getProperties().contains(Properties.POWERED)) {
                            return state.get(Properties.POWERED) ? BigDecimal.ONE : BigDecimal.ZERO;
                        } else {
                            var power = 0;
                            for (Direction direction : Direction.values()) {
                                power = Math.max(power, state.getStrongRedstonePower(world, parent, direction));
                            }
                            return BigDecimal.valueOf(power);
                        }
                    } else {
                        return logicBlockEntity.valueType().asNumber(logicBlockEntity.value());
                    }
                }).reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        this.markDirty();
    }
}
