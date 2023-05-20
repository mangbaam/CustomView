package com.mangbaam.currencytextfield

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
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

@Composable
fun MaterialCurrencyTextField(
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
    placeholder: @Composable (() -> Unit)? = null,
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
    TextField(
        value = amount,
        onValueChange = { value: String ->
            if (amount.contains('.') && value.count { c -> c == '.' } > 1) return@TextField
            val inputText = value.toBigDecimalOrZero
            if (inputText.equals(0)) {
                return@TextField
            }
            maxLength?.let { maxLength ->
                if (inputText.toString().length > maxLength) return@TextField
            }
            maxValue?.let { maxValue ->
                if (inputText > maxValue) return@TextField
            }
            amount = inputText.toString()
            onValueChanged(BigDecimal(amount.filterToNumber("0")))
            onTextChanged(visualText(amount, showSymbol, symbol, rearSymbol))
        },
        modifier = modifier,
        enabled = enabled,
        readOnly = !editable,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = CurrencyVisualTransformation(symbol, showSymbol, rearSymbol),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        keyboardActions = keyboardActions,
        singleLine = true,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )
}
