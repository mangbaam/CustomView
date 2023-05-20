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

/**
 * # 금액 입력 TextField
 *
 * @param initialAmount 화면에 표시될 초기 값. [maxValue] 보다 크거나 [maxLength] 보다 긴 경우, "0" 을 표시
 * @param maxValue 최대 값. 기본 값은 null 이며, null 일 경우 제약 없음
 * @param maxLength 최대 길이. 기본 값은 null 이며, null 일 경우 제약 없음
 * @param onTextChanged 표시되는 문자가 변경될 때의 콜백
 * @param onValueChanged 표시되는 금액이 변경될 때의 콜백
 * @param showSymbol 통화 기호를 표시할 지 여부
 * @param symbol 통화 기호. 기본 값은 현재 로케일의 통화 기호를 따른다
 * @param rearSymbol true 이면 통화 기호를 금액 뒤에 표시, false 이면 맨 앞에 표시. 기본 값은 true
 * @param textStyle 표시할 문자의 [TextStyle]
 * @param editable 수정 가능 여부. false 이면 수정할 수 없지만 포커즈를 받고 값을 복사할 수 있다. 값을 미리 채워두고 사용자가 수정할 수 없도록 만들 때 사용
 * @param enabled 사용 가능 여부. false 이면 수정할 수 없고, 포커즈도 받을 수 없으며 값을 선택할 수 없다.
 * @param interactionSource 이 TextField 의 [Interaction][androidx.compose.foundation.interaction.Interaction]의 스트림을 나타냄. [Interaction][androidx.compose.foundation.interaction.Interaction]을 관찰하거나 인터렉션을 커스텀하고 싶다면 [MutableInteractionSource]를 전달할 수 있다
 * */
@Composable
fun BasicCurrencyTextField(
    modifier: Modifier = Modifier,
    initialAmount: BigDecimal = BigDecimal.ZERO,
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
        maxValue != null && initialAmount > maxValue ||
        maxLength != null && initialAmount.toString().length > maxLength
    ) {
        "0"
    } else {
        initialAmount.toString()
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

/**
 * # 금액 입력 TextField - [String] 타입 지원
 *
 * @see BasicCurrencyTextField
 * */
@Composable
fun BasicCurrencyTextField(
    modifier: Modifier = Modifier,
    initialAmount: String = "0",
    maxValue: BigDecimal? = null,
    maxLength: Int? = null,
    onTextChanged: (String) -> Unit = {},
    onValueChanged: (String) -> Unit = {},
    showSymbol: Boolean = true,
    symbol: String = currencySymbol,
    rearSymbol: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    editable: Boolean = true,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val bigDecimalOnValueChangedHandler = { value: BigDecimal ->
        onValueChanged(value.toString())
    }

    BasicCurrencyTextField(
        modifier = modifier,
        initialAmount = BigDecimal(initialAmount.filterToNumber("0")),
        maxValue = maxValue,
        maxLength = maxLength,
        onTextChanged = onTextChanged,
        onValueChanged = bigDecimalOnValueChangedHandler,
        showSymbol = showSymbol,
        symbol = symbol,
        rearSymbol = rearSymbol,
        textStyle = textStyle,
        editable = editable,
        enabled = enabled,
        interactionSource = interactionSource,
    )
}

/**
 * 금액 입력 TextField - [Int] 타입 지원
 *
 * @param maxValue 최대 값. 기본 값은 null 이며, null 일 때 [Int]의 최대 값을 가진다. 이 값은 [Int]의 최대 값보다 클 수 없다
 * @param maxLength 최대 길이. 기본 값은 null 이며, null 일 때 ([Int]의 최대 값 길이 - 1) 을 가진다. 이 값은 ([Int]의 최대 값 길이 - 1) 보다 클 수 없다
 * @see BasicCurrencyTextField
 * */
@Composable
fun BasicCurrencyTextField(
    modifier: Modifier = Modifier,
    initialAmount: Int = 0,
    maxValue: Int? = null,
    maxLength: Int? = null,
    onTextChanged: (String) -> Unit = {},
    onValueChanged: (Int) -> Unit = {},
    showSymbol: Boolean = true,
    symbol: String = currencySymbol,
    rearSymbol: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    editable: Boolean = true,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val bigDecimalOnValueChangedHandler = { value: BigDecimal ->
        onValueChanged(value.toInt())
    }
    val maxLengthOfInt = Int.MAX_VALUE.toString().length

    BasicCurrencyTextField(
        modifier = modifier,
        initialAmount = BigDecimal(initialAmount),
        maxValue = maxValue?.let { BigDecimal(minOf(it, Int.MAX_VALUE)) }
            ?: BigDecimal(Int.MAX_VALUE),
        maxLength = maxLength?.let { minOf(it, maxLengthOfInt - 1) } ?: (maxLengthOfInt - 1),
        onTextChanged = onTextChanged,
        onValueChanged = bigDecimalOnValueChangedHandler,
        showSymbol = showSymbol,
        symbol = symbol,
        rearSymbol = rearSymbol,
        textStyle = textStyle,
        editable = editable,
        enabled = enabled,
        interactionSource = interactionSource,
    )
}

/**
 * # 금액 입력 TextField - [Long] 타입 지원
 *
 * @param maxValue 최대 값. 기본 값은 null 이며, null 일 때 [Long]의 최대 값을 가진다. 이 값은 [Long]의 최대 값보다 클 수 없다
 * @param maxLength 최대 길이. 기본 값은 null 이며, null 일 때 ([Long]의 최대 값 길이 - 1) 을 가진다. 이 값은 ([Long]의 최대 값 길이 - 1) 보다 클 수 없다
 * @see BasicCurrencyTextField
 * */
@Composable
fun BasicCurrencyTextField(
    modifier: Modifier = Modifier,
    initialAmount: Long = 0L,
    maxValue: Long? = null,
    maxLength: Int? = null,
    onTextChanged: (String) -> Unit = {},
    onValueChanged: (Long) -> Unit = {},
    showSymbol: Boolean = true,
    symbol: String = currencySymbol,
    rearSymbol: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    editable: Boolean = true,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val bigDecimalOnValueChangedHandler = { value: BigDecimal ->
        onValueChanged(value.toLong())
    }
    val maxLengthOfLong = Long.MAX_VALUE.toString().length

    BasicCurrencyTextField(
        modifier = modifier,
        initialAmount = BigDecimal(initialAmount),
        maxValue = maxValue?.let { BigDecimal(minOf(it, Long.MAX_VALUE)) }
            ?: BigDecimal(Long.MAX_VALUE),
        maxLength = maxLength?.let { minOf(it, maxLengthOfLong - 1) } ?: (maxLengthOfLong - 1),
        onTextChanged = onTextChanged,
        onValueChanged = bigDecimalOnValueChangedHandler,
        showSymbol = showSymbol,
        symbol = symbol,
        rearSymbol = rearSymbol,
        textStyle = textStyle,
        editable = editable,
        enabled = enabled,
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
