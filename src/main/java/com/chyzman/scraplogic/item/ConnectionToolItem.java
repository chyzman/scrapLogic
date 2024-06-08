package com.chyzman.scraplogic.item;

import com.chyzman.scraplogic.block.template.logic.LogicBlock;
import com.chyzman.scraplogic.item.component.PartialConnectionComponent;
import com.chyzman.scraplogic.item.template.ClickOverrider;
import com.chyzman.scraplogic.item.template.ConnectionDirection;
import com.chyzman.scraplogic.registry.ScrapLogicDataComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.chyzman.scraplogic.registry.ComponentInit.SCRAP_LOGIC_WORLD;

public class ConnectionToolItem extends Item implements ClickOverrider {

    public ConnectionToolItem(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public ActionResult onClick(ConnectionDirection direction, PlayerEntity player, World world, Hand hand, BlockPos pos) {
        var stack = player.getStackInHand(hand);
        var partialConnection = stack.getComponents().getOrDefault(ScrapLogicDataComponents.PARTIAL_CONNECTION, new PartialConnectionComponent(world.getRegistryKey()));
//        if (!(world.getBlockState(pos).getBlock() instanceof LogicBlock)) {
//            if (world.isClient) {
//                ClickOverrider.endClicks();
//            }
//            return ActionResult.FAIL;
//        }
        if (partialConnection.canAdd(pos, world, direction)) {
            if (!world.isClient) {
                stack.set(ScrapLogicDataComponents.PARTIAL_CONNECTION, partialConnection.addPosition(world, pos, direction));
            } else {
                ClickOverrider.endClicks();
            }
            return ActionResult.SUCCESS;
        } else if (partialConnection.shouldFinish(pos, world, direction)) {
            if (!world.isClient) {
                var worldComponent = SCRAP_LOGIC_WORLD.get(world);
                worldComponent.finishConnections(partialConnection, pos);
                stack.remove(ScrapLogicDataComponents.PARTIAL_CONNECTION);
            } else {
                ClickOverrider.endClicks();
            }
            return ActionResult.SUCCESS;
        } else if (partialConnection.positions().contains(pos)) {
            if (!world.isClient) {
                stack.set(ScrapLogicDataComponents.PARTIAL_CONNECTION, partialConnection.removePosition(world, pos));
            } else {
                ClickOverrider.endClicks();
            }
            return ActionResult.SUCCESS;
        } else {
            if (world.isClient) {
                ClickOverrider.endClicks();
            }
            return ActionResult.FAIL;
        }
    }
}
