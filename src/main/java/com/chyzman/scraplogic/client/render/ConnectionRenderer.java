package com.chyzman.scraplogic.client.render;

import com.chyzman.scraplogic.LogicConnection;
import com.chyzman.scraplogic.item.ConnectionToolItem;
import com.chyzman.scraplogic.item.component.ConnectionToolMode;
import com.chyzman.scraplogic.item.template.ConnectionDirection;
import com.mojang.blaze3d.systems.RenderSystem;
import io.wispforest.owo.ui.core.Color;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static com.chyzman.scraplogic.ScrapLogic.DISPLAYS_CONNECTIONS_TAG;
import static com.chyzman.scraplogic.registry.ComponentInit.SCRAP_LOGIC_WORLD;
import static com.chyzman.scraplogic.registry.ScrapLogicDataComponents.CONNECTION_TOOL_MODE;
import static com.chyzman.scraplogic.registry.ScrapLogicDataComponents.PARTIAL_CONNECTION;
import static com.chyzman.scraplogic.util.ScrapLogicRegistryHelper.id;
import static net.minecraft.client.render.RenderPhase.*;

public class ConnectionRenderer {

    public static final Identifier CONNECTION_TEXTURE = id("textures/connection.png");
    private static final Matrix4f BILLBOARD_MATRIX = new Matrix4f();

    private static final RenderLayer CONNECTION_LAYER = RenderLayer.of("connections", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 1536, false, true, RenderLayer.MultiPhaseParameters.builder()
            .program(RenderPhase.BEACON_BEAM_PROGRAM)
            .texture(new RenderPhase.Texture(CONNECTION_TEXTURE, false, false))
            .transparency(TRANSLUCENT_TRANSPARENCY)
            .writeMaskState(COLOR_MASK)
            .depthTest(ALWAYS_DEPTH_TEST)
            .build(false));


    public static void draw(List<LogicConnection> connections, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        var buffer = vertexConsumers.getBuffer(CONNECTION_LAYER);

        var width = 0.1f;

        var client = MinecraftClient.getInstance();
        var eyeStart = client.gameRenderer.getCamera().getPos();
        var eyeEnd = eyeStart.add(new Vec3d(0, 0, 32)
                .rotateX((float) -Math.toRadians(client.gameRenderer.getCamera().getPitch()))
                .rotateY((float) -Math.toRadians(client.gameRenderer.getCamera().getYaw())));

        LogicConnection closestConnection = null;
        double closestDistance = Double.MAX_VALUE;

        for (LogicConnection logicConnection : connections) {
            var distance = distanceBetweenLines(logicConnection.start().toCenterPos(), logicConnection.end().toCenterPos(), eyeStart, eyeEnd);
            if (distance < closestDistance) {
                closestConnection = logicConnection;
                closestDistance = distance;
            }
        }

        var player = client.player;
        ItemStack connectionTool = null;
        ConnectionToolMode mode;
        if (player.getMainHandStack().getItem() instanceof ConnectionToolItem) {
            connectionTool = player.getMainHandStack();
        } else if (player.getOffHandStack().getItem() instanceof ConnectionToolItem) {
            connectionTool = player.getOffHandStack();
        }
        if (connectionTool != null) {
            mode = connectionTool.getOrDefault(CONNECTION_TOOL_MODE, ConnectionToolMode.ADD);
        } else {
            mode = null;
        }

        var finalClosestConnection = closestDistance < 0.3 ? closestConnection : null;
        connections.forEach(connection -> drawConnection(
                connection.start().toCenterPos(),
                connection.end().toCenterPos(),
                connection.priority(),
                !ConnectionToolMode.targetsConnections(mode) || connection.equals(finalClosestConnection) ?
                        connection.color() :
                        new Color(connection.color().red(), connection.color().green(), connection.color().blue(), connection.color().alpha() / 10),
                width,
                matrices,
                buffer,
                light
        ));

        if (connectionTool != null && mode == ConnectionToolMode.ADD) {
            var partialConnection = connectionTool.get(PARTIAL_CONNECTION);
            if (partialConnection != null) {
                partialConnection.positions().forEach(pos -> {
                    var cursor = client.gameRenderer.getCamera().getPos();
                    cursor = cursor.add(new Vec3d(0, 0, 2)
                            .rotateX((float) -Math.toRadians(client.gameRenderer.getCamera().getPitch()))
                            .rotateY((float) -Math.toRadians(client.gameRenderer.getCamera().getYaw())));
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
    }

    private static void drawConnection(Vec3d start, Vec3d end, int priority, Color color, float width, MatrixStack matrices, VertexConsumer buffer, int light) {
        var length = start.distanceTo(end);
        var offset = MathHelper.fractionalPart(length) / 2 - (double) (MinecraftClient.getInstance().world.getTime() % 100) / 100;

        var camPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        var mid = findClosestPointOnLine(start, end, camPos);
        var startLength = start.distanceTo(mid);
        var endLength = mid.distanceTo(end);

        matrices.push();
        matrices.multiplyPositionMatrix(BILLBOARD_MATRIX.billboardCylindrical(
                mid.toVector3f(),
                camPos.toVector3f(),
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
                camPos.toVector3f(),
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

    public static void init() {
        WorldRenderEvents.LAST.register(context -> {
            RenderSystem.disableDepthTest();
            var client = MinecraftClient.getInstance();
            var mainHandStack = client.player.getMainHandStack();
            var offHandStack = client.player.getOffHandStack();
            var headStack = client.player.getEquippedStack(EquipmentSlot.HEAD);
            if (!mainHandStack.isIn(DISPLAYS_CONNECTIONS_TAG) && !offHandStack.isIn(DISPLAYS_CONNECTIONS_TAG) && !headStack.isIn(DISPLAYS_CONNECTIONS_TAG)) return;

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

    public static double distanceBetweenLines(
            Vec3d start1,
            Vec3d end1,
            Vec3d start2,
            Vec3d end2
    ) {
        var u = end1.subtract(start1);
        var v = end2.subtract(start2);
        var w = start1.subtract(start2);

        var a = u.dotProduct(u);
        var b = u.dotProduct(v);
        var c = v.dotProduct(v);
        var d = u.dotProduct(w);
        var e = v.dotProduct(w);

        var D = a * c - b * b;
        var sc = 0.0;
        var sN = 0.0;
        var sD = D;
        var tc = 0.0;
        var tN = 0.0;
        var tD = D;

        if (D < 1e-6) {
            sN = 0.0;
            sD = 1.0;
            tN = e;
            tD = c;
        } else {
            sN = (b * e - c * d);
            tN = (a * e - b * d);
            if (sN < 0.0) {
                sN = 0.0;
                tN = e;
                tD = c;
            } else if (sN > sD) {
                sN = sD;
                tN = e + b;
                tD = c;
            }
        }

        if (tN < 0.0) {
            tN = 0.0;
            if (-d < 0.0) {
                sN = 0.0;
            } else if (-d > a) {
                sN = sD;
            } else {
                sN = -d;
                sD = a;
            }
        } else if (tN > tD) {
            tN = tD;
            if ((-d + b) < 0.0) {
                sN = 0;
            } else if ((-d + b) > a) {
                sN = sD;
            } else {
                sN = (-d + b);
                sD = a;
            }
        }

        sc = (Math.abs(sN) < 1e-6 ? 0.0 : sN / sD);
        tc = (Math.abs(tN) < 1e-6 ? 0.0 : tN / tD);

        var dP = w.add(u.multiply(sc)).subtract(v.multiply(tc));
        return dP.length();
    }
}
