package com.chyzman.scraplogic.item.component;

import com.chyzman.scraplogic.item.template.ConnectionDirection;
import com.chyzman.scraplogic.util.ScrapLogicEndecs;
import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.endec.StructEndecBuilder;
import io.wispforest.owo.ui.core.Color;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class PartialConnectionComponent {

    private final List<BlockPos> positions;
    private final RegistryKey<World> dimension;
    private final ConnectionDirection direction;
    private final int priority;
    private final Color color;

    //region ENDEC STUFF

    public static final Endec<PartialConnectionComponent> ENDEC = StructEndecBuilder.of(
            Endec.ofCodec(BlockPos.CODEC).listOf().fieldOf("positions", PartialConnectionComponent::positions),
            Endec.ofCodec(World.CODEC).fieldOf("dimension", PartialConnectionComponent::dimension),
            Endec.forEnum(ConnectionDirection.class).fieldOf("direction", PartialConnectionComponent::direction),
            Endec.INT.fieldOf("priority", PartialConnectionComponent::priority),
            ScrapLogicEndecs.COLOR_ENDEC.fieldOf("color", PartialConnectionComponent::color),
            PartialConnectionComponent::new
    );

    //endregion

    public PartialConnectionComponent(
            List<BlockPos> positions,
            RegistryKey<World> dimension,
            ConnectionDirection direction,
            int priority,
            Color color
    ) {
        this.positions = positions;
        this.dimension = dimension;
        this.direction = direction;
        this.priority = priority;
        this.color = color;
    }

    public PartialConnectionComponent(
            RegistryKey<World> dimension,
            ConnectionDirection direction,
            int priority,
            Color color
    ) {
        this(
                List.of(),
                dimension,
                direction,
                priority,
                color
        );
    }

    public PartialConnectionComponent(RegistryKey<World> dimension) {
        this(
                dimension,
                ConnectionDirection.NONE,
                0,
                Color.WHITE
        );
    }

    //region GETTERS AND SETTERS

    public List<BlockPos> positions() {return positions;}

    public RegistryKey<World> dimension() {return dimension;}

    public ConnectionDirection direction() {return direction;}

    public int priority() {return priority;}

    public Color color() {return color;}

    //endregion

    public boolean isValid(BlockPos pos, World world) {
        return !this.positions.contains(pos) &&
                (this.dimension() == null || this.dimension().equals(world.getRegistryKey()));
    }

    public boolean canAdd(BlockPos pos, World world, ConnectionDirection direction) {
        return isValid(pos, world) &&
                (this.direction == ConnectionDirection.NONE || this.direction == direction);
    }

    public boolean shouldFinish(BlockPos pos, World world, ConnectionDirection direction) {
        return isValid(pos, world) &&
                !this.positions().isEmpty() &&
                this.direction() != ConnectionDirection.NONE &&
                this.direction() != direction;
    }


    public PartialConnectionComponent addPosition(
            World world,
            BlockPos pos,
            ConnectionDirection direction
    ) {
        var positions = new ArrayList<>(this.positions());
        positions.add(pos);
        return new PartialConnectionComponent(Collections.unmodifiableList(positions), world.getRegistryKey(), direction, 0, Color.WHITE);
    }

    @Nullable
    public PartialConnectionComponent removePosition(
            World world,
            BlockPos pos
    ) {
        var positions = new ArrayList<>(this.positions());
        positions.remove(pos);
        if (positions.isEmpty()) {
            return null;
        }
        return new PartialConnectionComponent(Collections.unmodifiableList(positions), world.getRegistryKey(), direction,  0, Color.WHITE);
    }
}
