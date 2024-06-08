package com.chyzman.scraplogic;

import com.chyzman.scraplogic.util.ScrapLogicEndecs;
import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.endec.StructEndecBuilder;
import io.wispforest.owo.ui.core.Color;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class LogicConnection {
    private BlockPos start;
    private BlockPos end;
    private int priority;
    private Color color;

    //region ENDEC STUFF

    public static final Endec<LogicConnection> ENDEC = StructEndecBuilder.of(
            Endec.ofCodec(BlockPos.CODEC).fieldOf("start", LogicConnection::start),
            Endec.ofCodec(BlockPos.CODEC).fieldOf("end", LogicConnection::end),
            Endec.INT.fieldOf("priority", LogicConnection::priority),
            ScrapLogicEndecs.COLOR_ENDEC.fieldOf("color", LogicConnection::color),
            LogicConnection::new
    );

    //endregion

    public LogicConnection(BlockPos start, BlockPos end, int priority, Color color) {
        this.start = start;
        this.end = end;
        this.priority = priority;
        this.color = color;
    }

    //region GETTERS AND SETTERS

    public BlockPos start() {
        return start;
    }

    public LogicConnection start(BlockPos start) {
        this.start = start;
        return this;
    }

    public BlockPos end() {
        return end;
    }

    public LogicConnection end(BlockPos end) {
        this.end = end;
        return this;
    }

    public int priority() {
        return priority;
    }

    public LogicConnection priority(int priority) {
        this.priority = priority;
        return this;
    }

    public Color color() {
        return color;
    }

    public LogicConnection color(Color color) {
        this.color = color;
        return this;
    }

    //endregion

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LogicConnection) obj;
        return Objects.equals(this.start, that.start) &&
                Objects.equals(this.end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
