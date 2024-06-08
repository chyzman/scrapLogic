package com.chyzman.scraplogic.block.template.logic;

import io.wispforest.owo.serialization.Endec;

import java.math.BigDecimal;
import java.util.function.Supplier;

public abstract class LogicBlockType<T> {
    public static final LogicBlockType<BigDecimal> NUMBER = new LogicBlockType<>(
            BigDecimal.class,
            Endec.STRING.xmap(val -> val.isEmpty() ? BigDecimal.ZERO : new BigDecimal(val), BigDecimal::toString),
            () -> BigDecimal.ZERO
    ) {
        @Override
        public boolean isTrueIsh(BigDecimal value) {
            return value.compareTo(BigDecimal.ZERO) != 0;
        }

        @Override
        public BigDecimal asNumber(BigDecimal value) {
            return value;
        }
    };
    public static final LogicBlockType<Boolean> BOOL = new LogicBlockType<>(
            Boolean.class,
            Endec.BOOLEAN,
            () -> false
    ) {
        @Override
        public boolean isTrueIsh(Boolean value) {
            return value;
        }

        @Override
        public BigDecimal asNumber(Boolean value) {
            return value ? BigDecimal.ONE : BigDecimal.ZERO;
        }
    };

    private final Class<T> clazz;
    private final Endec<T> endec;
    private final Supplier<T> defaultValue;

    public LogicBlockType(Class<T> clazz, Endec<T> endec, Supplier<T> defaultValue) {
        this.clazz = clazz;
        this.endec = endec;
        this.defaultValue = defaultValue;
    }

    public Class<T> type() {
        return clazz;
    }

    public Endec<T> endec() {
        return endec;
    }

    public T defaultValue() {
        return defaultValue.get();
    }

    public abstract boolean isTrueIsh(T value);

    public abstract BigDecimal asNumber(T value);
}
