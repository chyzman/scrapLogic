package com.chyzman.scraplogic.util;

import com.mojang.datafixers.util.Function4;
import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.ui.core.Color;

import java.util.List;
import java.util.function.Function;

public class ScrapLogicEndecs {

    public static final Endec<Color> COLOR_ENDEC = vectorEndec(
            "Color",
            Endec.FLOAT,
            Color::new,
            Color::red,
            Color::green,
            Color::blue,
            Color::alpha
    );

    private static <C, V> Endec<V> vectorEndec(
            String name,
            Endec<C> componentEndec,
            Function4<C, C, C, C, V> constructor,
            Function<V, C> xGetter,
            Function<V, C> yGetter,
            Function<V, C> zGetter,
            Function<V, C> wGetter
    ) {
        return componentEndec.listOf().validate(ints -> {
            if (ints.size() != 4) {
                throw new IllegalStateException(name + " array must have four elements");
            }
        }).xmap(
                components -> constructor.apply(components.get(0), components.get(1), components.get(2), components.get(3)),
                vector -> List.of(xGetter.apply(vector), yGetter.apply(vector), zGetter.apply(vector), wGetter.apply(vector))
        );
    }
}
