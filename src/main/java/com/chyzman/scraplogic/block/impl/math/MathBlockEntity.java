package com.chyzman.scraplogic.block.impl.math;

import ch.obermuhlner.math.big.BigDecimalMath;
import com.chyzman.scraplogic.block.template.logic.LogicBlockEntity;
import com.chyzman.scraplogic.block.template.logic.LogicBlockType;
import com.chyzman.scraplogic.block.template.logic.LogicGateMode;
import com.chyzman.scraplogic.block.template.logic.MathBlockMode;
import com.chyzman.scraplogic.registry.ScrapLogicBlocks;
import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.endec.KeyedEndec;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.math.BigDecimal;

import static com.chyzman.scraplogic.registry.ComponentInit.SCRAP_LOGIC_WORLD;

@SuppressWarnings("unchecked")
public class MathBlockEntity extends LogicBlockEntity<BigDecimal> {
    private MathBlockMode mode = MODE_KEY.defaultValue();

    //region ENDEC STUFF

    private static final KeyedEndec<MathBlockMode> MODE_KEY = Endec.forEnum(MathBlockMode.class).keyed("mode", MathBlockMode.ADD);

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

    public MathBlockEntity(BlockPos pos, BlockState state) {
        super(ScrapLogicBlocks.Entities.MATH_BLOCK_ENTITY, pos, state);
        this.value = LogicBlockType.NUMBER.defaultValue();
    }

    public MathBlockEntity(BlockPos pos, BlockState state, BigDecimal value) {
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

    @SuppressWarnings("rawtypes")
    @Override
    public void tickServer() {
        if (!shouldTick()) return;
        super.tickServer();
        var world = getWorld();
        if (world == null) return;
        var worldComponent = SCRAP_LOGIC_WORLD.get(world);
        this.value = mode.calculate(world, worldComponent.getParents(this.pos)
                .stream()
                .map(connection -> {
                    var parent = connection.start();
                    var entity = world.getBlockEntity(parent);
                    if (!(entity instanceof LogicBlockEntity logicBlockEntity)) {
                        var state = world.getBlockState(parent);
                        if (state.getProperties().contains(Properties.POWERED)) {
                            return LogicBlockType.BOOL.asNumber(state.get(Properties.POWERED));
                        } else {
                            var power = 0;
                            for (Direction direction : Direction.values()) {
                                power = Math.max(power, world.getStrongRedstonePower(parent, direction));
                            }
                            return BigDecimal.valueOf(power);
                        }
                    } else {
                        return logicBlockEntity.valueType().asNumber(logicBlockEntity.value());
                    }
                }).toList()
        );
        this.markDirty();
    }

    public MathBlockMode getMode() {
        return mode;
    }
}
