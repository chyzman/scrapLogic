package com.chyzman.scraplogic.block.template.logic;

import java.util.List;
import java.util.function.Function;

public enum LogicGateMode {
    AND("and", inputs -> !inputs.isEmpty() && inputs.stream().allMatch(aBoolean -> aBoolean)),
    OR("or", inputs -> !inputs.isEmpty() && inputs.stream().anyMatch(aBoolean -> aBoolean)),
    XOR("xor", inputs -> !inputs.isEmpty() && inputs.stream().filter(aBoolean -> aBoolean).count() % 2 == 1),
    NAND("nand", inputs -> !inputs.isEmpty() && !inputs.stream().allMatch(aBoolean -> aBoolean)),
    NOR("nor", inputs -> !inputs.isEmpty() && inputs.stream().noneMatch(aBoolean -> aBoolean)),
    XNOR("xnor", inputs -> !inputs.isEmpty() && inputs.stream().filter(aBoolean -> aBoolean).count() % 2 == 0);

    public final String name;
    public final Function<List<Boolean>, Boolean> function;

    LogicGateMode(String name, Function<List<Boolean>, Boolean> function) {
        this.name = name;
        this.function = function;
    }
}
