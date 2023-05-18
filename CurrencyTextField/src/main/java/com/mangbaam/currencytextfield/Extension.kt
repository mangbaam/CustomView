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

/**
 * @return 최초로 등장하는 [delimiter] 를 기준으로 두 문자열로 쪼갠 [Pair]
 * */
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

/**
 * @return 문자열의 뒤에서부터 [size] 만큼 묶어서 [separator] 로 구분된 문자열 반환
 * */
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
    // integer: 정수부, decimal: 소수부
    var (integer, decimal) = filterToNumber("0").divide('.')
    decimal = decimal.dropLastWhile { c -> c == '0' }

    val stringBuffer = StringBuffer(integer.reverseChunked(chunkSize))
    if (decimal.isNotBlank()) {
        stringBuffer
            .append('.')
            .append(decimal)
    }
    return stringBuffer.toString()
}
