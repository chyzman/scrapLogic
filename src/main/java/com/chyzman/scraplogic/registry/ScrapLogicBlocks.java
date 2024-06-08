package com.chyzman.scraplogic.registry;

import com.chyzman.scraplogic.block.impl.counter.CounterBlock;
import com.chyzman.scraplogic.block.impl.counter.CounterBlockEntity;
import com.chyzman.scraplogic.block.impl.gate.GateBlock;
import com.chyzman.scraplogic.block.impl.gate.GateBlockEntity;
import com.chyzman.scraplogic.block.impl.math.MathBlock;
import com.chyzman.scraplogic.block.impl.math.MathBlockEntity;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ScrapLogicBlocks implements BlockRegistryContainer {

    public static final Block COUNTER = new CounterBlock(AbstractBlock.Settings.copy(Blocks.STONE));
    public static final Block LOGIC_GATE = new GateBlock(AbstractBlock.Settings.copy(Blocks.STONE));
    public static final Block MATH_BLOCK = new MathBlock(AbstractBlock.Settings.copy(Blocks.STONE));


    public static class Entities implements AutoRegistryContainer<BlockEntityType<?>> {

        public static final BlockEntityType<CounterBlockEntity> COUNTER_BLOCK_ENTITY = make(CounterBlockEntity::new, COUNTER);
        public static final BlockEntityType<GateBlockEntity> LOGIC_GATE_BLOCK_ENTITY = make(GateBlockEntity::new, LOGIC_GATE);
        public static final BlockEntityType<MathBlockEntity> MATH_BLOCK_ENTITY = make(MathBlockEntity::new, MATH_BLOCK);


        private static <T extends BlockEntity> BlockEntityType<T> make(BlockEntityType.BlockEntityFactory<T> factory, Block... blocks) {
            return BlockEntityType.Builder.create(factory, blocks).build();
        }

        @Override
        public Registry<BlockEntityType<?>> getRegistry() {
            return Registries.BLOCK_ENTITY_TYPE;
        }

        @Override
        public Class<BlockEntityType<?>> getTargetFieldType() {
            return AutoRegistryContainer.conform(BlockEntityType.class);
        }
    }
}
