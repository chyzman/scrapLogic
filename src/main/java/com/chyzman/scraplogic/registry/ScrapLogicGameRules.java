package com.chyzman.scraplogic.registry;

import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameRules;

import java.math.MathContext;

import static com.chyzman.scraplogic.ScrapLogic.MATH_CONTEXT;
import static com.chyzman.scraplogic.ScrapLogic.MODID;
import static com.chyzman.scraplogic.util.ScrapLogicRegistryHelper.id;

public class ScrapLogicGameRules {

    public static final CustomGameRuleCategory SCRAP_LOGIC_CATEGORY = new CustomGameRuleCategory(
            id("scrap_logic"),
            Text.translatable("gamerule.scrap_logic.category.scrap_logic").formatted(Formatting.BOLD, Formatting.YELLOW)
    );

    public static final GameRules.Key<GameRules.IntRule> LOGIC_TICK_SPEED = register("logicTickSpeed", GameRuleFactory.createIntRule(1, 0));

    public static final GameRules.Key<GameRules.IntRule> LOGIC_PRECISION = register("logicPrecision", GameRuleFactory.createIntRule(34, 0,
            (minecraftServer, intRule) -> MATH_CONTEXT = new MathContext(intRule.get())
    ));


    public static void init() {

    }

    public static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Type<T> type) {
        return GameRuleRegistry.register(MODID + "." + name, SCRAP_LOGIC_CATEGORY, type);
    }
}
