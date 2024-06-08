package com.chyzman.scraplogic.component;

import com.chyzman.scraplogic.LogicConnection;
import com.chyzman.scraplogic.item.component.PartialConnectionComponent;
import io.wispforest.owo.serialization.endec.KeyedEndec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.*;

import static com.chyzman.scraplogic.registry.ComponentInit.SCRAP_LOGIC_WORLD;

public class ScrapLogicWorldComponent implements Component, AutoSyncedComponent, ServerTickingComponent {
    private final World holder;

    private Set<LogicConnection> connections = CONNECTIONS.defaultValue();

    //region ENDEC STUFF

    private static final KeyedEndec<Set<LogicConnection>> CONNECTIONS = LogicConnection.ENDEC.setOf().keyed("Connections", new HashSet<>());


    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        connections = tag.get(CONNECTIONS);
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.put(CONNECTIONS, connections);
    }

    //endregion

    public ScrapLogicWorldComponent(World holder) {this.holder = holder;}

    public List<LogicConnection> getConnections() {
        return connections
                .stream()
                .sorted(Comparator.comparing(LogicConnection::priority))
                .toList();
    }

    public List<LogicConnection> getParents(BlockPos pos) {
        return connections
                .stream()
                .filter(connection -> connection.end().equals(pos))
                .sorted(Comparator.comparing(LogicConnection::priority))
                .toList();
    }

    public List<LogicConnection> getChildren(BlockPos pos) {
        return connections
                .stream()
                .filter(connection -> connection.start().equals(pos))
                .sorted(Comparator.comparing(LogicConnection::priority))
                .toList();
    }

    public void finishConnections(PartialConnectionComponent connections, BlockPos pos) {
        for (var connection : connections.positions()) {
            switch (connections.direction()) {
                case IN -> this.connections.add(new LogicConnection(pos, connection, connections.priority(), connections.color()));
                case OUT -> this.connections.add(new LogicConnection(connection, pos, connections.priority(), connections.color()));
            }
        }
        SCRAP_LOGIC_WORLD.sync(holder);
    }

    @Override
    public void serverTick() {
        var tempConnections = new HashSet<>(connections);
        connections.removeIf(logicConnection -> !isValidConnection(holder, logicConnection));
        if (connections.equals(tempConnections)) return;
        SCRAP_LOGIC_WORLD.sync(holder);
    }

    public static boolean isValidConnection(World world, LogicConnection connection) {
        return !world.getBlockState(connection.start()).isAir() && !world.getBlockState(connection.end()).isAir();
    }
}
