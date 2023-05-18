package com.mangbaam.currencytextfield

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.math.BigDecimal

@Composable
fun CurrencyTextField(
    modifier: Modifier = Modifier,
    initAmount: BigDecimal = BigDecimal.ZERO,
    maxValue: BigDecimal? = null,
    maxLength: Int? = null,
    onTextChanged: (String) -> Unit = {},
    onValueChanged: (BigDecimal) -> Unit = {},
    showSymbol: Boolean = true,
    symbol: String = currencySymbol,
    rearSymbol: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    editable: Boolean = true,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val availInitAmount = if (
        maxValue != null && initAmount > maxValue ||
        maxLength != null && initAmount.toString().length > maxLength
    ) {
        "0"
    } else {
        initAmount.toString()
    }
    var amount by remember { mutableStateOf(availInitAmount) }

    BasicTextField(
        value = amount,
        onValueChange = { value: String ->
            if (amount.contains('.') && value.count { c -> c == '.' } > 1) return@BasicTextField
            val inputText = value.toBigDecimalOrZero
            if (inputText.equals(0)) {
                return@BasicTextField
            }
            maxLength?.let { maxLength ->
                if (inputText.toString().length > maxLength) return@BasicTextField
            }
            maxValue?.let { maxValue ->
                if (inputText > maxValue) return@BasicTextField
            }
            amount = inputText.toString()
            onValueChanged(BigDecimal(amount.filterToNumber("0")))
            onTextChanged(visualText(amount, showSymbol, symbol, rearSymbol))
        },
        modifier = Modifier
            .wrapContentSize()
            .then(modifier),
        enabled = enabled,
        readOnly = !editable,
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        singleLine = true,
        visualTransformation = CurrencyVisualTransformation(symbol, showSymbol, rearSymbol),
        interactionSource = interactionSource,
    )
}

internal fun visualText(
    amount: String,
    showSymbol: Boolean,
    symbol: String,
    rearSymbol: Boolean,
): String {
    val sb = StringBuffer()
    if (showSymbol && !rearSymbol) {
        sb.append(symbol)
    }
    sb.append(amount.toCurrencyFormat())
    if (showSymbol && rearSymbol) {
        sb.append(symbol)
    }
    return sb.toString()
}

internal class CurrencyVisualTransformation(
    val symbol: String,
    val showSymbol: Boolean,
    val rearSymbol: Boolean,
    val chunkSize: Int = DEFAULT_CHUNK_SIZE,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val currencyFormat = text.text.toCurrencyFormat(chunkSize)
        return TransformedText(
            text = AnnotatedString(
                visualText(
                    text.text,
                    showSymbol,
                    symbol,
                    rearSymbol,
                ),
            ),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    val rightLength = text.lastIndex - offset
                    val commasAtRight = rightLength / chunkSize
                    val transformedIndex = currencyFormat.lastIndex - (rightLength + commasAtRight)

                    return transformedIndex.coerceIn(0..currencyFormat.length).let {
                        if (showSymbol && !rearSymbol) {
                            it + symbol.length
                        } else {
                            it
                        }
                    }
                }

                override fun transformedToOriginal(offset: Int): Int {
                    val commas = (text.lastIndex / chunkSize).coerceAtLeast(0)
                    val rightOffset = currencyFormat.lastIndex - offset
                    val commasAtRight = rightOffset / (chunkSize + 1)

                    return if (showSymbol && !rearSymbol) {
                        (offset - (commas - commasAtRight) - symbol.length).coerceIn(0..text.lastIndex + symbol.length)
                    } else {
                        (offset - (commas - commasAtRight)).coerceIn(0..text.length)
                    }
                }
            },
        )
    }

    companion object {
        const val DEFAULT_CHUNK_SIZE = 3
    }
}
