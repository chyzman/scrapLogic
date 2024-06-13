package com.chyzman.scraplogic.registry;

import com.chyzman.scraplogic.item.component.ConnectionToolMode;
import com.chyzman.scraplogic.item.component.PartialConnectionComponent;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.SerializationAttributes;
import io.wispforest.owo.serialization.SerializationContext;
import net.minecraft.component.DataComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ScrapLogicDataComponents implements AutoRegistryContainer<DataComponentType<?>> {

    public static DataComponentType<PartialConnectionComponent> PARTIAL_CONNECTION = DataComponentType.<PartialConnectionComponent>builder().codec(PartialConnectionComponent.ENDEC.codec(SerializationContext.attributes(SerializationAttributes.HUMAN_READABLE))).packetCodec(PartialConnectionComponent.ENDEC.packetCodec()).build();
    public static DataComponentType<ConnectionToolMode> CONNECTION_TOOL_MODE = DataComponentType.<ConnectionToolMode>builder().codec(Endec.forEnum(ConnectionToolMode.class).codec()).packetCodec(Endec.forEnum(ConnectionToolMode.class).packetCodec()).build();

    @Override
    public Registry<DataComponentType<?>> getRegistry() {
        return Registries.DATA_COMPONENT_TYPE;
    }

    @Override
    public Class<DataComponentType<?>> getTargetFieldType() {
        return AutoRegistryContainer.conform(DataComponentType.class);
    }
}
