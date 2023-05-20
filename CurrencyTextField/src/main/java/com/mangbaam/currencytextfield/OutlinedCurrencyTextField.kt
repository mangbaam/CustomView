package com.mangbaam.currencytextfield

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
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
 * @param label 텍스트 필드 위에 표시할 문구
 * @param leadingIcon 텍스트 필드의 맨 앞에 표시될 아이콘
 * @param trailingIcon 텍스트 필드의 맨 뒤에 표시될 아이콘
 * @param prefix 텍스트 필드에 표시될 문자 맨 앞에 보여질 prefix
 * @param suffix 텍스트 필드에 표시될 문자 맨 뒤에 보여질 suffix
 * @param supportingText 텍스트 필드 밑에 표시할 문구
 * @param isError 현재 값이 에러인지 여부. true 이면 [label]과 하단 인디케이터, [trailingIcon] 이 에러 색상으로 표시됨
 * @param textStyle 표시할 문자의 [TextStyle]
 * @param editable 수정 가능 여부. false 이면 수정할 수 없지만 포커즈를 받고 값을 복사할 수 있다. 값을 미리 채워두고 사용자가 수정할 수 없도록 만들 때 사용
 * @param enabled 사용 가능 여부. false 이면 수정할 수 없고, 포커즈도 받을 수 없으며 값을 선택할 수 없다.
 * @param interactionSource 이 TextField 의 [Interaction][androidx.compose.foundation.interaction.Interaction]의 스트림을 나타냄. [Interaction][androidx.compose.foundation.interaction.Interaction]을 관찰하거나 인터렉션을 커스텀하고 싶다면 [MutableInteractionSource]를 전달할 수 있다
 * @param shape 텍스트 필드의 모양
 * @param colors 각 상황별 텍스트 필드의 색상을 표시하는 [TextFieldColors]. [TextFieldDefaults.colors] 참고
 * */
@Composable
fun OutlinedCurrencyTextField(
    modifier: Modifier = Modifier,
    initialAmount: BigDecimal = BigDecimal.ZERO,
    maxValue: BigDecimal? = null,
    maxLength: Int? = null,
    onTextChanged: (String) -> Unit = {},
    onValueChanged: (BigDecimal) -> Unit = {},
    showSymbol: Boolean = true,
    symbol: String = currencySymbol,
    rearSymbol: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    editable: Boolean = true,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
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
    OutlinedTextField(
        value = amount,
        onValueChange = { value: String ->
            if (amount.contains('.') && value.count { c -> c == '.' } > 1) return@OutlinedTextField
            val inputText = value.toBigDecimalOrZero
            if (inputText.equals(0)) {
                return@OutlinedTextField
            }
            maxLength?.let { maxLength ->
                if (inputText.toString().length > maxLength) return@OutlinedTextField
            }
            maxValue?.let { maxValue ->
                if (inputText > maxValue) return@OutlinedTextField
            }
            amount = inputText.toString()
            onValueChanged(BigDecimal(amount.filterToNumber("0")))
            onTextChanged(visualText(amount, showSymbol, symbol, rearSymbol))
        },
        modifier = modifier,
        enabled = enabled,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        textStyle = textStyle,
        readOnly = !editable,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
        visualTransformation = CurrencyVisualTransformation(symbol, showSymbol, rearSymbol),
    )
}

@Composable
fun OutlinedCurrencyTextField(
    modifier: Modifier = Modifier,
    initialAmount: String = "O",
    maxValue: BigDecimal? = null,
    maxLength: Int? = null,
    onTextChanged: (String) -> Unit = {},
    onValueChanged: (String) -> Unit = {},
    showSymbol: Boolean = true,
    symbol: String = currencySymbol,
    rearSymbol: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    editable: Boolean = true,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
) {
    val bigDecimalOnValueChangedHandler = { value: BigDecimal ->
        onValueChanged(value.toString())
    }

    OutlinedCurrencyTextField(
        modifier = modifier,
        initialAmount = BigDecimal(initialAmount.filterToNumber("0")),
        maxValue = maxValue,
        maxLength = maxLength,
        onTextChanged = onTextChanged,
        onValueChanged = bigDecimalOnValueChangedHandler,
        showSymbol = showSymbol,
        symbol = symbol,
        rearSymbol = rearSymbol,
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        textStyle = textStyle,
        editable = editable,
        enabled = enabled,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )
}

@Composable
fun OutlinedCurrencyTextField(
    modifier: Modifier = Modifier,
    initialAmount: Int = 0,
    maxValue: Int? = null,
    maxLength: Int? = null,
    onTextChanged: (String) -> Unit = {},
    onValueChanged: (Int) -> Unit = {},
    showSymbol: Boolean = true,
    symbol: String = currencySymbol,
    rearSymbol: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    editable: Boolean = true,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
) {
    val bigDecimalOnValueChangedHandler = { value: BigDecimal ->
        onValueChanged(value.toInt())
    }
    val maxLengthOfInt = Int.MAX_VALUE.toString().length

    OutlinedCurrencyTextField(
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
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        textStyle = textStyle,
        editable = editable,
        enabled = enabled,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )
}

@Composable
fun OutlinedCurrencyTextField(
    modifier: Modifier = Modifier,
    initialAmount: Long = 0L,
    maxValue: Long? = null,
    maxLength: Int? = null,
    onTextChanged: (String) -> Unit = {},
    onValueChanged: (Long) -> Unit = {},
    showSymbol: Boolean = true,
    symbol: String = currencySymbol,
    rearSymbol: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    editable: Boolean = true,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
) {
    val bigDecimalOnValueChangedHandler = { value: BigDecimal ->
        onValueChanged(value.toLong())
    }
    val maxLengthOfLong = Long.MAX_VALUE.toString().length

    OutlinedCurrencyTextField(
        modifier = modifier,
        initialAmount = BigDecimal(initialAmount),
        maxValue = maxValue?.let { BigDecimal(minOf(it, Long.MAX_VALUE)) }
            ?: BigDecimal(Int.MAX_VALUE),
        maxLength = maxLength?.let { minOf(it, maxLengthOfLong - 1) } ?: (maxLengthOfLong - 1),
        onTextChanged = onTextChanged,
        onValueChanged = bigDecimalOnValueChangedHandler,
        showSymbol = showSymbol,
        symbol = symbol,
        rearSymbol = rearSymbol,
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        textStyle = textStyle,
        editable = editable,
        enabled = enabled,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )
}
