package com.chyzman.scraplogic.client;

import com.chyzman.scraplogic.client.render.ConnectionRenderer;
import io.wispforest.owo.ui.core.Color;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import static com.chyzman.scraplogic.registry.ComponentInit.SCRAP_LOGIC_WORLD;
import static com.chyzman.scraplogic.util.ScrapLogicRegistryHelper.id;

public class ClientEventListeners {

    public static void init() {
        ConnectionRenderer.init();
    }
}
