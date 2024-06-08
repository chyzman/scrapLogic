package com.chyzman.scraplogic;

import com.chyzman.scraplogic.item.ScrapLogicItemGroup;
import com.chyzman.scraplogic.registry.ScrapLogicBlocks;
import com.chyzman.scraplogic.registry.ScrapLogicDataComponents;
import com.chyzman.scraplogic.registry.ScrapLogicGameRules;
import com.chyzman.scraplogic.registry.ScrapLogicItems;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import java.math.MathContext;

import static com.chyzman.scraplogic.util.ScrapLogicRegistryHelper.id;

public class ScrapLogic implements ModInitializer {
    public static final String MODID = "scrap_logic";

    public static final TagKey<Item> DISPLAYS_CONNECTIONS_TAG = TagKey.of(RegistryKeys.ITEM, id("displays_connections"));

    public static MathContext MATH_CONTEXT = MathContext.DECIMAL128;

    @Override
    public void onInitialize() {
        ScrapLogicItemGroup.register();

        AutoRegistryContainer.register(ScrapLogicItems.class, MODID, false);
        AutoRegistryContainer.register(ScrapLogicBlocks.class, MODID, true);
        AutoRegistryContainer.register(ScrapLogicDataComponents.class, MODID, false);

        ScrapLogicGameRules.init();

        ServerEventListeners.init();

        ScrapLogicItemGroup.group().initialize();
    }
}
