package com.chyzman.scraplogic.registry;

import org.ladysnake.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class ComponentInit implements ChunkComponentInitializer {

    public static final ComponentKey<InternalsChunkComponent> INTERNALS_CHUNK = ComponentRegistry.getOrCreate(id("internals_chunk"), InternalsChunkComponent.class);


    @Override
    public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {

    }
}
