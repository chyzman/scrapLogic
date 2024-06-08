package com.chyzman.scraplogic.block.impl.math;


import com.chyzman.scraplogic.block.template.logic.LogicBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class MathBlockEntityRenderer implements BlockEntityRenderer<MathBlockEntity> {
    public MathBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(
            MathBlockEntity entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay
    ) {
        var client = MinecraftClient.getInstance();
        matrices.push();
        var orientation = entity.getCachedState().get(LogicBlock.ORIENTATION);
        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(orientation.getFacing().getOpposite().getRotationQuaternion());
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));
        if (orientation.getRotation().getAxis().isHorizontal()) {
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(((orientation.getFacing() == Direction.DOWN && orientation.getRotation().getAxis() != Direction.Axis.X)? orientation.getRotation().getOpposite() : orientation.getRotation()).getHorizontal() * 90));
        }
        matrices.translate(0, 0, -0.501);
        matrices.scale(-0.025F, -0.025F, 0.025F);
        var value = entity.value().toString();
        client.textRenderer.draw(
                entity.getMode().name(),
                -client.textRenderer.getWidth(entity.getMode().name()) / 2f,
                -client.textRenderer.fontHeight/2f + client.textRenderer.fontHeight / 2f,
                -1,
                false,
                matrices.peek().getPositionMatrix(),
                vertexConsumers,
                TextRenderer.TextLayerType.NORMAL,
                0,
                client.world.getLightLevel(entity.getPos().offset(orientation.getFacing())) * 16
        );
        client.textRenderer.draw(
                value,
                -client.textRenderer.getWidth(value) / 2f,
                -client.textRenderer.fontHeight/2f - client.textRenderer.fontHeight / 2f,
                -1,
                false,
                matrices.peek().getPositionMatrix(),
                vertexConsumers,
                TextRenderer.TextLayerType.NORMAL,
                0,
                client.world.getLightLevel(entity.getPos().offset(orientation.getFacing())) * 16
        );
        matrices.pop();
    }
}
