package com.chyzman.scraplogic.client.render;

import com.chyzman.scraplogic.LogicConnection;
import com.chyzman.scraplogic.item.template.ConnectionDirection;
import com.mojang.blaze3d.systems.RenderSystem;
import io.wispforest.owo.ui.core.Color;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static com.chyzman.scraplogic.ScrapLogic.DISPLAYS_CONNECTIONS_TAG;
import static com.chyzman.scraplogic.registry.ComponentInit.SCRAP_LOGIC_WORLD;
import static com.chyzman.scraplogic.registry.ScrapLogicDataComponents.PARTIAL_CONNECTION;
import static com.chyzman.scraplogic.util.ScrapLogicRegistryHelper.id;

public class ConnectionRenderer {

    public static final Identifier CONNECTION_TEXTURE = id("textures/connection.png");
    private static final Matrix4f BILLBOARD_MATRIX = new Matrix4f();

    public static void draw(List<LogicConnection> connections, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        var buffer = vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(CONNECTION_TEXTURE, true));

        var width = 0.1f;

        connections.forEach(connection -> drawConnection(
                connection.start().toCenterPos(),
                connection.end().toCenterPos(),
                connection.priority(),
                connection.color(),
                width,
                matrices,
                buffer,
                light
        ));

        var player = MinecraftClient.getInstance().player;
        var connectionTools = new ArrayList<ItemStack>();
        if (player.getMainHandStack().contains(PARTIAL_CONNECTION)) connectionTools.add(player.getMainHandStack());
        if (player.getOffHandStack().contains(PARTIAL_CONNECTION)) connectionTools.add(player.getOffHandStack());
        for (ItemStack connectionTool : connectionTools) {
            var partialConnection = connectionTool.get(PARTIAL_CONNECTION);
            partialConnection.positions().forEach(pos -> {
                var cursor = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
                cursor = cursor.add(new Vec3d(0, 0, 2)
                        .rotateX((float) -Math.toRadians(MinecraftClient.getInstance().gameRenderer.getCamera().getPitch()))
                        .rotateY((float) -Math.toRadians(MinecraftClient.getInstance().gameRenderer.getCamera().getYaw())));
                var position = pos.toCenterPos();
                var start = partialConnection.direction().equals(ConnectionDirection.IN) ? cursor : position;
                var end = partialConnection.direction().equals(ConnectionDirection.IN) ? position : cursor;
                drawConnection(
                        start,
                        end,
                        0,
                        partialConnection.direction() == ConnectionDirection.IN ? Color.RED : Color.GREEN,
                        width,
                        matrices,
                        buffer,
                        light
                );
            });
        }
    }

    private static void drawConnection(Vec3d start, Vec3d end, int priority, Color color, float width, MatrixStack matrices, VertexConsumer buffer, int light) {
        var length = start.distanceTo(end);
        var offset = MathHelper.fractionalPart(length) / 2 - (double) (MinecraftClient.getInstance().world.getTime() % 100) / 100;

        var mid = findClosestPointOnLine(start, end, MinecraftClient.getInstance().gameRenderer.getCamera().getPos());
        var startLength = start.distanceTo(mid);
        var endLength = mid.distanceTo(end);

        matrices.push();
        matrices.multiplyPositionMatrix(BILLBOARD_MATRIX.billboardCylindrical(
                mid.toVector3f(),
                MinecraftClient.getInstance().gameRenderer.getCamera().getPos().toVector3f(),
                start.subtract(mid).toVector3f()
        ));

        buffer.vertex(matrices.peek().getPositionMatrix(), width, 0, 0)
                .color(color.argb())
                .texture((float) offset, 1)
                .light(light)
                .normal(matrices.peek(), 0, 1, 0)
                .next();

        buffer.vertex(matrices.peek().getPositionMatrix(), width, 1, 0)
                .color(color.argb())
                .texture((float) -(startLength - offset), 1)
                .light(light)
                .normal(matrices.peek(), 0, 1, 0)
                .next();

        buffer.vertex(matrices.peek().getPositionMatrix(), -width, 1, 0)
                .color(color.argb())
                .texture((float) -(startLength - offset), 0)
                .light(light)
                .normal(matrices.peek(), 0, 1, 0)
                .next();

        buffer.vertex(matrices.peek().getPositionMatrix(), -width, 0, 0)
                .color(color.argb())
                .texture((float) offset, 0)
                .light(light)
                .normal(matrices.peek(), 0, 1, 0)
                .next();
        matrices.pop();

        matrices.push();
        matrices.multiplyPositionMatrix(BILLBOARD_MATRIX.billboardCylindrical(
                mid.toVector3f(),
                MinecraftClient.getInstance().gameRenderer.getCamera().getPos().toVector3f(),
                end.subtract(mid).toVector3f()
        ));

        buffer.vertex(matrices.peek().getPositionMatrix(), width, 0, 0)
                .color(color.argb())
                .texture((float) offset, 1)
                .light(light)
                .normal(matrices.peek(), 0, 1, 0)
                .next();

        buffer.vertex(matrices.peek().getPositionMatrix(), width, 1, 0)
                .color(color.argb())
                .texture((float) (endLength + offset), 1)
                .light(light)
                .normal(matrices.peek(), 0, 1, 0)
                .next();

        buffer.vertex(matrices.peek().getPositionMatrix(), -width, 1, 0)
                .color(color.argb())
                .texture((float) (endLength + offset), 0)
                .light(light)
                .normal(matrices.peek(), 0, 1, 0)
                .next();

        buffer.vertex(matrices.peek().getPositionMatrix(), -width, 0, 0)
                .color(color.argb())
                .texture((float) offset, 0)
                .light(light)
                .normal(matrices.peek(), 0, 1, 0)
                .next();
        matrices.pop();
    }

    private static void drawConnectionQuad(Vec3d start, Vec3d end, Color color, float width, MatrixStack matrices, VertexConsumer buffer, int light, double offset, double length) {

    }

    public static void init() {
        WorldRenderEvents.LAST.register(context -> {
            RenderSystem.disableDepthTest();
            var client = MinecraftClient.getInstance();
            var mainHandStack = client.player.getMainHandStack();
            var offHandStack = client.player.getOffHandStack();
            if (!mainHandStack.isIn(DISPLAYS_CONNECTIONS_TAG) && !offHandStack.isIn(DISPLAYS_CONNECTIONS_TAG)) return;

            var matrices = context.matrixStack();
            var camera = context.camera().getPos();
            var worldComponent = SCRAP_LOGIC_WORLD.get(context.world());

            matrices.push();
            matrices.translate(-camera.x, -camera.y, -camera.z);
            draw(worldComponent.getConnections(), context.matrixStack(), context.consumers(), LightmapTextureManager.MAX_LIGHT_COORDINATE);
            matrices.pop();

            ((VertexConsumerProvider.Immediate) context.consumers()).draw();
        });
    }

    public static Vec3d findClosestPointOnLine(Vec3d start, Vec3d end, Vec3d point) {
        var direction = end.subtract(start).normalize();
        var pointVector = point.subtract(start);
        double projectionLength = direction.dotProduct(pointVector);
        if (projectionLength < 0) {
            return start;
        } else if (projectionLength > start.distanceTo(end)) {
            return end;
        } else {
            return direction.multiply(projectionLength).add(start);
        }
    }
}
