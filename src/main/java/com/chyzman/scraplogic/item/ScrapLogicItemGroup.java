package com.chyzman.scraplogic.item;

import com.chyzman.scraplogic.registry.ScrapLogicBlocks;
import com.chyzman.scraplogic.registry.ScrapLogicItems;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.gui.ItemGroupButton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Items;

import java.util.List;
import java.util.Objects;

import static com.chyzman.scraplogic.util.ScrapLogicRegistryHelper.id;

public class ScrapLogicItemGroup {

    private static OwoItemGroup GROUP;

    public static void register() {
        GROUP = OwoItemGroup.builder(id("scrap_logic"), () -> Icon.of(ScrapLogicItems.CONNECTION_TOOL)).initializer(ScrapLogicItemGroup::initializeGroup).build();
    }

    public static OwoItemGroup group() {
        return Objects.requireNonNull(GROUP, "Scrap Logic item group not initialized");
    }

    @Environment(EnvType.CLIENT)
    private static void initializeGroup(OwoItemGroup group) {
        group.addCustomTab(Icon.of(ScrapLogicItems.CONNECTION_TOOL), "main", (context, entries) -> {
            entries.addAll(List.of(
                    ScrapLogicItems.CONNECTION_TOOL.getDefaultStack(),
                    ScrapLogicBlocks.COUNTER.asItem().getDefaultStack(),
                    ScrapLogicBlocks.LOGIC_GATE.asItem().getDefaultStack(),
                    ScrapLogicBlocks.MATH_BLOCK.asItem().getDefaultStack()
            ));
        }, true);

        group.addButton(ItemGroupButton.github(group, "https://github.com/chyzman"));
        group.addButton(ItemGroupButton.curseforge(group, "https://www.curseforge.com/members/chyzman5253/projects"));
        group.addButton(ItemGroupButton.modrinth(group, "https://modrinth.com/user/chyzman"));
    }
}
