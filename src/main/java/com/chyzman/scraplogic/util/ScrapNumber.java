package com.chyzman.scraplogic.util;

import com.google.common.math.BigDecimalMath;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ScrapNumber {
    private BigDecimal value;

    //region CONSTRUCTORS

    public ScrapNumber(BigDecimal value) {
        this.value = value;
    }

    public ScrapNumber(int value) {
        this.value = new BigDecimal(value);
    }

    public ScrapNumber(long value) {
        this.value = new BigDecimal(value);
    }

    public ScrapNumber(double value) {
        this.value = new BigDecimal(value);
    }

    public ScrapNumber(String value) {
        this.value = new BigDecimal(value);
    }

    //endregion


    //region ADDITION

    public ScrapNumber add(ScrapNumber number) {
        return new ScrapNumber(value.add(number.value));
    }

    public ScrapNumber add(int number) {
        return new ScrapNumber(value.add(new BigDecimal(number)));
    }

    public ScrapNumber add(long number) {
        return new ScrapNumber(value.add(new BigDecimal(number)));
    }

    public ScrapNumber add(double number) {
        return new ScrapNumber(value.add(new BigDecimal(number)));
    }

    public ScrapNumber add(String number) {
        return new ScrapNumber(value.add(new BigDecimal(number)));
    }

    //endregion

    //region SUBTRACTION

    public ScrapNumber subtract(ScrapNumber number) {
        return new ScrapNumber(value.subtract(number.value));
    }

    public ScrapNumber subtract(int number) {
        return new ScrapNumber(value.subtract(new BigDecimal(number)));
    }

    public ScrapNumber subtract(long number) {
        return new ScrapNumber(value.subtract(new BigDecimal(number)));
    }

    public ScrapNumber subtract(double number) {
        return new ScrapNumber(value.subtract(new BigDecimal(number)));
    }

    public ScrapNumber subtract(String number) {
        return new ScrapNumber(value.subtract(new BigDecimal(number)));
    }

    //endregion

    //region MULTIPLICATION

    public ScrapNumber multiply(ScrapNumber number) {
        return new ScrapNumber(value.multiply(number.value));
    }

    public ScrapNumber multiply(int number) {
        return new ScrapNumber(value.multiply(new BigDecimal(number)));
    }

    public ScrapNumber multiply(long number) {
        return new ScrapNumber(value.multiply(new BigDecimal(number)));
    }

    public ScrapNumber multiply(double number) {
        return new ScrapNumber(value.multiply(new BigDecimal(number)));
    }

    public ScrapNumber multiply(String number) {
        return new ScrapNumber(value.multiply(new BigDecimal(number)));
    }

    //endregion

    //region DIVISION

    public ScrapNumber divide(ScrapNumber number) {
        return new ScrapNumber(value.divide(number.value, RoundingMode.DOWN));
    }

    public ScrapNumber divide(int number) {
        return new ScrapNumber(value.divide(new BigDecimal(number), RoundingMode.DOWN));
    }

    public ScrapNumber divide(long number) {
        return new ScrapNumber(value.divide(new BigDecimal(number), RoundingMode.DOWN));
    }

    public ScrapNumber divide(double number) {
        return new ScrapNumber(value.divide(new BigDecimal(number), RoundingMode.DOWN));
    }

    public ScrapNumber divide(String number) {
        return new ScrapNumber(value.divide(new BigDecimal(number), RoundingMode.DOWN));
    }

    //endregion

    //region MODULO

    public ScrapNumber modulo(ScrapNumber number) {
        return new ScrapNumber(value.remainder(number.value));
    }

    public ScrapNumber modulo(int number) {
        return new ScrapNumber(value.remainder(new BigDecimal(number)));
    }

    public ScrapNumber modulo(long number) {
        return new ScrapNumber(value.remainder(new BigDecimal(number)));
    }

    public ScrapNumber modulo(double number) {
        return new ScrapNumber(value.remainder(new BigDecimal(number)));
    }

    public ScrapNumber modulo(String number) {
        return new ScrapNumber(value.remainder(new BigDecimal(number)));
    }

    //endregion

    //region POWER

    public ScrapNumber pow(ScrapNumber number) {
        return new ScrapNumber(value.pow(number.value.intValue()));
    }
}
