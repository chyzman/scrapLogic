package com.chyzman.scraplogic;

import com.chyzman.scraplogic.item.template.ClickOverrider;
import com.chyzman.scraplogic.item.template.ConnectionDirection;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.ActionResult;

import java.math.MathContext;

import static com.chyzman.scraplogic.registry.ScrapLogicGameRules.LOGIC_PRECISION;

public class ServerEventListeners {

    public static void init() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (player.getStackInHand(hand).getItem() instanceof ClickOverrider clickOverrider) {
                return clickOverrider.onClick(ConnectionDirection.OUT, player, world, hand, hitResult.getBlockPos());
            }
            return ActionResult.PASS;
        });

        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (player.getStackInHand(hand).getItem() instanceof ClickOverrider clickOverrider) {
                return clickOverrider.onClick(ConnectionDirection.IN, player, world, hand, pos);
            }
            return ActionResult.PASS;
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> ScrapLogic.MATH_CONTEXT = new MathContext(server.getGameRules().getInt(LOGIC_PRECISION)));
    }
}
