package com.chyzman.scraplogic.block.template.logic;

import ch.obermuhlner.math.big.BigDecimalMath;
import net.minecraft.world.World;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.chyzman.scraplogic.ScrapLogic.MATH_CONTEXT;

public enum MathBlockMode {
    // x: sum of all inputs
    ADD(
            "add",
            (inputs, mathContext) ->
                    inputs.stream().reduce(BigDecimal.ZERO, (bd, bd2) -> bd.add(bd2, mathContext))
    ),
    // 1: -input
    // x: subtract all inputs from the first input
    SUBTRACT(
            "subtract",
            (inputs, mathContext) ->
                    inputs.size() == 1 ?
                            inputs.getFirst().negate() :
                            inputs.stream().skip(1).reduce(inputs.getFirst(), (bd, bd2) -> bd.subtract(bd2, mathContext))
    ),

    // x: product of all inputs
    MULTIPLY("multiply", -1,
            (inputs, mathContext) -> inputs.stream().reduce(BigDecimal.ONE, BigDecimal::multiply)
    ),
    // 1: 1/input
    // x: divide the first input by all other inputs
    DIVIDE("divide", 1,
            (inputs, mathContext) ->
                    inputs.getFirst().compareTo(BigDecimal.ZERO) > 0 ?
                            inputs.size() == 1 ?
                                    BigDecimal.ONE.divide(inputs.getFirst(), mathContext) :
                                    inputs.stream().skip(1).filter(bigDecimal -> bigDecimal.compareTo(BigDecimal.ZERO) != 0).reduce(inputs.getFirst(), (bd, bd2) -> bd.divide(bd2, mathContext)) :
                            BigDecimal.ZERO
    ),

    // x: remainder of the first input divided by the second input divided by the third input...
    MOD("mod", 2,
            (inputs, mathContext) -> inputs.stream().skip(1).filter(bigDecimal -> bigDecimal.compareTo(BigDecimal.ZERO) > 0).reduce(inputs.getFirst(), (bd, bd2) -> bd.remainder(bd2, mathContext))
    ),

    // 1: input^2
    // 2: first input to the power of the all other inputs
    POW(
            "pow",
            (inputs, mathContext) -> inputs.size() == 1 ?
                    inputs.getFirst().pow(2, mathContext) :
                    inputs.stream().skip(1).reduce(inputs.getFirst(), (bd, bd2) -> BigDecimalMath.pow(bd, bd2, mathContext))
    ),
    // 1: natural log of input
    // x: log base last input of log base second to last input of log base third to last input...
    LOG(
            "log",
            (inputs, mathContext) ->
                    inputs.getFirst().compareTo(BigDecimal.ZERO) > 0 ?
                            inputs.size() == 1 ?
                                    BigDecimalMath.log(inputs.getFirst(), mathContext) :
                                    inputs.stream().skip(1).filter(bigDecimal -> bigDecimal.compareTo(BigDecimal.ZERO) > 0).reduce(inputs.getFirst(), (bd, bd2) -> BigDecimalMath.log(bd, mathContext).divide(BigDecimalMath.log(bd2, mathContext), mathContext)) :
                            BigDecimal.ZERO
    ),
    // 1: sqrt(input)
    // x: first input to the root of the second input to the root of the third input...
    ROOT(
            "root",
            (inputs, mathContext) -> inputs.size() == 1 ?
                    BigDecimalMath.sqrt(inputs.getFirst(), mathContext) :
                    inputs.stream().skip(1).reduce(inputs.getFirst(), (bd, bd2) -> BigDecimalMath.root(bd, bd2, mathContext))
    ),

    // 1: abs(input)
    ABS("abs", 1, 1,
            (inputs, mathContext) -> inputs.getFirst().abs()
    ),

    // 1: !input
    FACTORIAL("factorial", 1, 1,
            (inputs, mathContext) -> BigDecimalMath.factorial(inputs.getFirst().intValue())
    ),

    // 1: sin(input)
    SIN("sin", 1, 1,
            (inputs, mathContext) -> BigDecimalMath.sin(inputs.getFirst(), mathContext)
    ),
    // 1: cos(input)
    COS("cos", 1, 1,
            (inputs, mathContext) -> BigDecimalMath.cos(inputs.getFirst(), mathContext)
    ),
    // 1: tan(input)
    TAN("tan", 1, 1,
            (inputs, mathContext) -> BigDecimalMath.tan(inputs.getFirst(), mathContext)
    ),

    // 1: asin(input)
    ASIN("asin", 1, 1,
            (inputs, mathContext) ->
                    (inputs.getFirst().compareTo(BigDecimal.ZERO) >= 0 && inputs.getFirst().compareTo(BigDecimal.ONE) <= 0) ?
                            BigDecimalMath.asin(inputs.getFirst(), mathContext) :
                            BigDecimal.ZERO
    ),
    // 1: acos(input)
    ACOS("acos", 1, 1,
            (inputs, mathContext) ->
                    (inputs.getFirst().compareTo(BigDecimal.ZERO) >= 0 && inputs.getFirst().compareTo(BigDecimal.ONE) <= 0) ?
                            BigDecimalMath.acos(inputs.getFirst(), mathContext) :
                            BigDecimal.ZERO
    ),
    // 1: atan(input)
    ATAN("atan", 1, 1,
            (inputs, mathContext) -> BigDecimalMath.atan(inputs.getFirst(), mathContext)
    ),

    // 1: sinh(input)
    SINH("sinh", 1, 1,
            (inputs, mathContext) -> BigDecimalMath.sinh(inputs.getFirst(), mathContext)
    ),
    // 1: cosh(input)
    COSH("cosh", 1, 1,
            (inputs, mathContext) -> BigDecimalMath.cosh(inputs.getFirst(), mathContext)
    ),
    // 1: tanh(input)
    TANH("tanh", 1, 1,
            (inputs, mathContext) -> BigDecimalMath.tanh(inputs.getFirst(), mathContext)
    ),

    // 1: asinh(input)
    ASINH("asinh", 1, 1,
            (inputs, mathContext) ->
                    inputs.getFirst().compareTo(BigDecimal.ONE) >= 0 ?
                            BigDecimalMath.asinh(inputs.getFirst(), mathContext) :
                            BigDecimal.ZERO
    ),
    // 1: acosh(input)
    ACOSH("acosh", 1, 1,
            (inputs, mathContext) ->
                    inputs.getFirst().compareTo(BigDecimal.ONE) >= 0 ?
                            BigDecimalMath.acosh(inputs.getFirst(), mathContext) :
                            BigDecimal.ZERO
    ),
    // 1: atanh(input)
    ATANH("atanh", 1, 1,
            (inputs, mathContext) ->
                    (inputs.getFirst().compareTo(BigDecimal.ZERO) >= 0 && inputs.getFirst().compareTo(BigDecimal.ONE) <= 0) ?
                            BigDecimalMath.atanh(inputs.getFirst(), mathContext) :
                            BigDecimal.ZERO
    ),

    // 1: input in degrees
    DEGREES("degrees", 1, 1,
            (inputs, mathContext) -> inputs.getFirst().multiply(BigDecimal.valueOf(180)).divide(BigDecimalMath.pi(mathContext), mathContext)
    ),

    // x: largest input
    MAX(
            "max",
            (inputs, mathContext) -> inputs.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO)
    ),
    // x: smallest input
    MIN(
            "min",
            (inputs, mathContext) -> inputs.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO)
    ),

    // x: sum of all inputs divided by the number of inputs
    MEAN("mean",
            (inputs, mathContext) -> inputs.stream().reduce(BigDecimal.ZERO, (bd, bd2) -> bd.add(bd2, mathContext)).divide(BigDecimal.valueOf(inputs.size()), mathContext)
    ),
    // x: middle value of all inputs
    MEDIAN("median", (inputs, mathContext) -> {
        var sorted = inputs.stream().sorted().toList();
        var size = sorted.size();
        return size % 2 == 0 ? sorted.get(size / 2).add(sorted.get(size / 2 - 1), mathContext).divide(BigDecimal.TWO, mathContext) : sorted.get(size / 2);
    }),
    // x: most common value of all inputs
    MODE("mode", (inputs, mathContext) -> {
        var map = inputs.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        var max = map.values().stream().max(Long::compareTo).orElse(0L);
        return map.entrySet().stream().filter(e -> e.getValue().equals(max)).map(Map.Entry::getKey).findFirst().orElse(BigDecimal.ZERO);
    }),

    // 1: round(input)
    // 2: round input to the nearest multiple of the second input
    ROUND("round", 1, 2,
            (inputs, mathContext) -> inputs.size() == 1 ?
                    inputs.getFirst().round(mathContext) :
                    inputs.getFirst().divide(inputs.get(1), mathContext).setScale(0, RoundingMode.HALF_UP).multiply(inputs.get(1))
    ),
    // 1: floor(input)
    // 2: floor input to the nearest multiple of the second input
    FLOOR("floor", 1, 2,
            (inputs, mathContext) -> inputs.size() == 1 ?
                    inputs.getFirst().setScale(0, RoundingMode.FLOOR) :
                    inputs.getFirst().divide(inputs.get(1), mathContext).setScale(0, RoundingMode.FLOOR).multiply(inputs.get(1))
    ),
    // 1: ceil(input)
    // 2: ceil input to the nearest multiple of the second input
    CEIL("ceil", 1, 2,
            (inputs, mathContext) -> inputs.size() == 1 ?
                    inputs.getFirst().setScale(0, RoundingMode.CEILING) :
                    inputs.getFirst().divide(inputs.get(1), mathContext).setScale(0, RoundingMode.CEILING).multiply(inputs.get(1))
    ),




    ;

    public final String name;
    public final int minInputs;
    public final int maxInputs;
    private final BiFunction<List<BigDecimal>, MathContext, BigDecimal> function;

    MathBlockMode(
            String name,
            int minInputs,
            int maxInputs,
            BiFunction<List<BigDecimal>, MathContext, BigDecimal> function
    ) {
        this.name = name;
        this.minInputs = minInputs;
        this.maxInputs = maxInputs;
        this.function = function;
    }

    MathBlockMode(
            String name, int minInputs, BiFunction<List<BigDecimal>, MathContext, BigDecimal> function
    ) {
        this(name, minInputs, -1, function);
    }

    MathBlockMode(
            String name, BiFunction<List<BigDecimal>, MathContext, BigDecimal> function
    ) {
        this(name, 1, function);
    }


    public BigDecimal calculate(World world, List<BigDecimal> inputs) {
        try {
            return inputs.isEmpty() || (minInputs >= 0 && inputs.size() < minInputs) || (maxInputs >= 0 && inputs.size() > maxInputs) ? BigDecimal.ZERO : function.apply(inputs, MATH_CONTEXT);
        } catch (Exception ignored) {
            return BigDecimal.ZERO;
        }
    }
}
