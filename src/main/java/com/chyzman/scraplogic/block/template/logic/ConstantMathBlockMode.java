package com.chyzman.scraplogic.block.template.logic;

import ch.obermuhlner.math.big.BigDecimalMath;
import com.chyzman.scraplogic.registry.ScrapLogicGameRules;
import net.minecraft.world.World;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.function.Function;

public enum ConstantMathBlockMode {
    PI("pi", input -> BigDecimalMath.pi(new MathContext(input))),
    E("e",input -> BigDecimalMath.e(new MathContext(input)));

    public final String name;
    private final Function<Integer, BigDecimal> function;

    ConstantMathBlockMode(String name, Function<Integer, BigDecimal> function) {
        this.name = name;
        this.function = function;
    }

    public BigDecimal calculate(World world, List<BigDecimal> inputs) {
        if (inputs.isEmpty()) return this.function.apply(world.getGameRules().getInt(ScrapLogicGameRules.LOGIC_PRECISION));
        return this.function.apply(MathBlockMode.ADD.calculate(world, inputs).intValue());
    }
}
