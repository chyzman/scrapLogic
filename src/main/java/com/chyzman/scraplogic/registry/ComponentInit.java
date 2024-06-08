package com.chyzman.scraplogic.registry;

import com.chyzman.scraplogic.component.ScrapLogicWorldComponent;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

import static com.chyzman.scraplogic.util.ScrapLogicRegistryHelper.id;

public class ComponentInit implements WorldComponentInitializer {

    public static final ComponentKey<ScrapLogicWorldComponent> SCRAP_LOGIC_WORLD = ComponentRegistry.getOrCreate(id("world"), ScrapLogicWorldComponent.class);


    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(SCRAP_LOGIC_WORLD, ScrapLogicWorldComponent::new);

    }
}
