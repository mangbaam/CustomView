package com.mangbaam.currencytextfield

import java.math.BigDecimal

sealed interface OverbalanceStrategy {
    enum class Amount : OverbalanceStrategy {
        Default, MaxValue, Ignore
    }

    enum class Text : OverbalanceStrategy {
        Default, DropFirst, DropLast, Ignore
    }
}

internal fun applyMaxValue(
    value: BigDecimal,
    maxValue: BigDecimal,
    strategy: OverbalanceStrategy.Amount,
): BigDecimal {
    if (value <= maxValue) return value
    return when (strategy) {
        OverbalanceStrategy.Amount.Default -> BigDecimal.ZERO
        OverbalanceStrategy.Amount.MaxValue -> maxValue
        OverbalanceStrategy.Amount.Ignore -> maxValue
    }
}

internal fun applyMaxLength(
    value: BigDecimal,
    maxLength: Int,
    strategy: OverbalanceStrategy.Text,
): String {
    if (value.toString().length < maxLength) return value.toString()
    val overTextCount = value.toString().length - maxLength
    return when (strategy) {
        OverbalanceStrategy.Text.Default -> "0"
        OverbalanceStrategy.Text.DropFirst -> value.toString().drop(overTextCount)
        OverbalanceStrategy.Text.DropLast -> value.toString().dropLast(overTextCount)
        OverbalanceStrategy.Text.Ignore -> value.toString().dropLast(overTextCount)
    }
}
