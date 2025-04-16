package com.example.cobarecipesapp

import java.math.BigDecimal
import java.math.RoundingMode

fun String.toBigDecimalOrZero(): BigDecimal {
    return try {
        BigDecimal(this)
    } catch (e: NumberFormatException) {
        BigDecimal.ZERO
    }
}

fun String.multiply(factor: Int): BigDecimal {
    return this.toBigDecimalOrZero().multiply(factor.toBigDecimal())
}

fun BigDecimal.toRoundedString(scale: Int = 1): String {
    return this.setScale(scale, RoundingMode.HALF_UP)
        .stripTrailingZeros()
        .toPlainString()
}