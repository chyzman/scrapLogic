package com.chyzman.scraplogic.block.template.logic;

import ch.obermuhlner.math.big.BigDecimalMath;
import net.minecraft.world.World;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.chyzman.scraplogic.ScrapLogic.MATH_CONTEXT;

public enum MathBlockMode {
    ADD("add", -1,(inputs, mathContext) -> inputs.stream().reduce(BigDecimal.ZERO, (bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2, mathContext))),
    SUBTRACT("subtract", -1, (inputs, mathContext) -> inputs.stream().reduce(BigDecimal.ZERO, BigDecimal::subtract)),
    MULTIPLY("multiply", -1, (inputs, mathContext) -> inputs.stream().reduce(BigDecimal.ONE, BigDecimal::multiply)),
    DIVIDE("divide", -1, (inputs, mathContext) -> inputs.stream().reduce(BigDecimal.ONE, BigDecimal::divide)),

    MOD("mod", 2, (inputs, mathContext) -> inputs.getFirst().remainder(inputs.get(1))),

    POW("pow", -1,(inputs, mathContext) -> inputs.stream().reduce(BigDecimal.ONE, (a, b) -> BigDecimalMath.pow(a, b, mathContext))),
    LOG("log", 2, (inputs, mathContext) -> BigDecimalMath.log(inputs.getFirst(), mathContext)),
    ROOT("root", 2, (inputs, mathContext) -> BigDecimalMath.root(inputs.getFirst(), inputs.get(1), mathContext)),

    ABS("abs", 1, (inputs, mathContext) -> inputs.getFirst().abs()),
    FACTORIAL("factorial", 1, (inputs, mathContext) -> BigDecimalMath.factorial(inputs.getFirst(), mathContext)),

    SIN("sin", 1, (inputs, mathContext) -> BigDecimalMath.sin(inputs.getFirst(), mathContext)),
    COS("cos", 1, (inputs, mathContext) -> BigDecimalMath.cos(inputs.getFirst(), mathContext)),
    TAN("tan", 1, (inputs, mathContext) -> BigDecimalMath.tan(inputs.getFirst(), mathContext)),

    ASIN("asin", 1, (inputs, mathContext) -> BigDecimalMath.asin(inputs.getFirst(), mathContext)),
    ACOS("acos", 1, (inputs, mathContext) -> BigDecimalMath.acos(inputs.getFirst(), mathContext)),
    ATAN("atan", 1, (inputs, mathContext) -> BigDecimalMath.atan(inputs.getFirst(), mathContext)),

    SINH("sinh", 1, (inputs, mathContext) -> BigDecimalMath.sinh(inputs.getFirst(), mathContext)),
    COSH("cosh", 1, (inputs, mathContext) -> BigDecimalMath.cosh(inputs.getFirst(), mathContext)),
    TANH("tanh", 1, (inputs, mathContext) -> BigDecimalMath.tanh(inputs.getFirst(), mathContext)),

    ASINH("asinh", 1, (inputs, mathContext) -> BigDecimalMath.asinh(inputs.getFirst(), mathContext)),
    ACOSH("acosh", 1, (inputs, mathContext) -> BigDecimalMath.acosh(inputs.getFirst(), mathContext)),
    ATANH("atanh", 1, (inputs, mathContext) -> BigDecimalMath.atanh(inputs.getFirst(), mathContext)),

    MAX("max", -1, (inputs, mathContext) -> inputs.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO)),
    MIN("min", -1, (inputs, mathContext) -> inputs.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO)),

    MEAN("mean", -1, (inputs, mathContext) -> inputs.stream().reduce(BigDecimal.ZERO, (bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2, mathContext)).divide(BigDecimal.valueOf(inputs.size()), mathContext)),
    MEDIAN("median", -1, (inputs, mathContext) -> {
        var sorted = inputs.stream().sorted().toList();
        var size = sorted.size();
        return size % 2 == 0 ? sorted.get(size / 2).add(sorted.get(size / 2 - 1), mathContext).divide(BigDecimal.TWO, mathContext) : sorted.get(size / 2);
    }),
    MODE("mode", -1, (inputs, mathContext) -> {
        var map = inputs.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        var max = map.values().stream().max(Long::compareTo).orElse(0L);
        return map.entrySet().stream().filter(e -> e.getValue().equals(max)).map(Map.Entry::getKey).findFirst().orElse(BigDecimal.ZERO);
    });

    public final String name;
    public final int requiredInputs;
    private final BiFunction<List<BigDecimal>, MathContext, BigDecimal> function;

    MathBlockMode(String name, int requiredInputs, BiFunction<List<BigDecimal>, MathContext, BigDecimal> function) {
        this.name = name;
        this.requiredInputs = requiredInputs;
        this.function = function;
    }

    public BigDecimal calculate(World world, List<BigDecimal> inputs) {
        return inputs.isEmpty() || (requiredInputs >= 0 && inputs.size() != requiredInputs) ? BigDecimal.ZERO : function.apply(inputs, MATH_CONTEXT);
    }
}
