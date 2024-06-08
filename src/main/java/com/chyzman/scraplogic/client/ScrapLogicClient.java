package com.chyzman.scraplogic.client;

import com.chyzman.scraplogic.block.impl.counter.CounterBlockEntityRenderer;
import com.chyzman.scraplogic.block.impl.gate.GateBlockEntityRenderer;
import com.chyzman.scraplogic.block.impl.math.MathBlockEntityRenderer;
import com.chyzman.scraplogic.registry.ScrapLogicBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class ScrapLogicClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerBlockEntityRenderers();

        ClientEventListeners.init();
    }

    private void registerBlockEntityRenderers() {
        BlockEntityRendererFactories.register(ScrapLogicBlocks.Entities.COUNTER_BLOCK_ENTITY, CounterBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ScrapLogicBlocks.Entities.LOGIC_GATE_BLOCK_ENTITY, GateBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ScrapLogicBlocks.Entities.MATH_BLOCK_ENTITY, MathBlockEntityRenderer::new);
    }
}
