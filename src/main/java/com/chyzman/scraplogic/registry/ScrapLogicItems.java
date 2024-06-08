package com.chyzman.scraplogic.registry;

import com.chyzman.scraplogic.block.impl.counter.CounterBlock;
import com.chyzman.scraplogic.block.impl.counter.CounterBlockEntity;
import com.chyzman.scraplogic.item.ConnectionToolItem;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ScrapLogicItems implements ItemRegistryContainer {

    public static final Item CONNECTION_TOOL = new ConnectionToolItem(new Item.Settings());

}
