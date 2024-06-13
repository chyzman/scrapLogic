package com.chyzman.scraplogic.item.component;

public enum ConnectionToolMode {
    ADD(false),
    REMOVE(true);

    public final boolean targetsConnections;

    ConnectionToolMode(boolean targetsConnections) {
        this.targetsConnections = targetsConnections;
    }

    public static boolean targetsConnections(ConnectionToolMode mode) {
        return mode != null && mode.targetsConnections;
    }
}
