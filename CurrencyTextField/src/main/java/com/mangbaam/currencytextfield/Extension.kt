package com.mangbaam.currencytextfield

import java.math.BigDecimal
import java.text.NumberFormat

val currencySymbol
    get() = NumberFormat.getCurrencyInstance().currency?.symbol ?: "원"

fun String.filterToNumber(): String = filter { c -> c.isDigit() || c == '.' }

fun String.filterToNumber(defaultValue: String): String = filterToNumber().let {
    it.ifBlank { defaultValue }
}

val String.toBigDecimalOrZero: BigDecimal
    get() = filterToNumber().toBigDecimalOrNull() ?: BigDecimal.ZERO

fun String.divide(delimiter: Char, ignoreCase: Boolean = false): Pair<String, String> {
    val index = indexOf(delimiter, ignoreCase = ignoreCase)
    return if (index == -1) {
        Pair(this, "")
    } else {
        Pair(
            substring(0 until index),
            substring(index + 1..lastIndex),
        )
    }
}

fun String.reverseChunked(size: Int, separator: Char = ','): String {
    val remain = length % size
    return if (remain > 0) {
        val left = substring(0 until remain)
        val right = substring(remain..lastIndex)
        val stringBuffer = StringBuffer(left)
        if (right.isNotBlank()) {
            stringBuffer
                .append(separator)
                .append(right.chunkedSequence(size).joinToString(separator.toString()))
        }
        stringBuffer.toString()
    } else {
        chunkedSequence(size).joinToString(separator.toString())
    }
}

fun String.toCurrencyFormat(chunkSize: Int = 3): String {
    var (integer, decimal) = filterToNumber("0").divide('.')
    decimal = decimal.dropLastWhile { c ->
        c == '0'
    }
    val stringBuffer = StringBuffer(integer.reverseChunked(chunkSize))
    if (decimal.isNotBlank()) {
        stringBuffer
            .append('.')
            .append(decimal)
    }
    return stringBuffer.toString()
}
