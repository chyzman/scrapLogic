package com.chyzman.scraplogic.item.template;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ClickOverrider {
    ActionResult onClick(ConnectionDirection direction, PlayerEntity player, World world, Hand hand, BlockPos pos);

    @Environment(EnvType.CLIENT)
    static void endClicks() {
        MinecraftClient.getInstance().options.useKey.setPressed(false);
        MinecraftClient.getInstance().options.attackKey.setPressed(false);
    }
}
