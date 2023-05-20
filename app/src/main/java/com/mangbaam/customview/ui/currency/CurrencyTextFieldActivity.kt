package com.mangbaam.customview.ui.currency

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mangbaam.currencytextfield.BasicCurrencyTextField
import com.mangbaam.currencytextfield.CurrencyTextField
import com.mangbaam.customview.ui.currency.CurrencyTextFieldActivity.Companion.TAG
import com.mangbaam.customview.ui.theme.CustomViewTheme
import java.math.BigDecimal

class CurrencyTextFieldActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    CurrencyTextFieldScreen()
                }
            }
        }
    }

    companion object {
        const val TAG = "로그"
    }
}

@Composable
fun CurrencyTextFieldScreen() {
    var displayed by remember {
        mutableStateOf("")
    }
    val initValue = 100
    var amount by remember {
        mutableStateOf(initValue.toString())
    }
    val onValueChanged: (BigDecimal) -> Unit = {
        Log.d(TAG, "onValueChanged: $it")
        amount = it.toString()
    }
    val onTextChanged = { text: String ->
        Log.d(TAG, "onTextChanged: $text")
        displayed = text
    }
    Column {
        InfoText(text = "표시된 값: $displayed")
        InfoText(text = "금액: $amount")
        BasicTextField(initValue, onTextChanged, onValueChanged)
        KoreanTextField(initValue, onTextChanged, onValueChanged)
        MaterialTextField()
    }
}

@Composable
private fun BasicTextField(
    initValue: Int,
    onTextChanged: (String) -> Unit,
    onValueChanged: (BigDecimal) -> Unit,
) {
    BasicCurrencyTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.LightGray)
            .padding(30.dp),
        initialAmount = BigDecimal(initValue),
        textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End),
        onTextChanged = onTextChanged,
        onValueChanged = onValueChanged,
        rearSymbol = true,
        maxValue = null,
        maxLength = null,
    )
}

@Composable
fun KoreanTextField(
    initValue: Int,
    onTextChanged: (String) -> Unit,
    onValueChanged: (BigDecimal) -> Unit,
) {
    BasicCurrencyTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .border(2.dp, Color.Blue)
            .padding(24.dp),
        initialAmount = BigDecimal(initValue),
        textStyle = MaterialTheme.typography.labelMedium,
        onTextChanged = onTextChanged,
        onValueChanged = onValueChanged,
        symbol = "원",
    )
}

@Composable
fun MaterialTextField() {
    CurrencyTextField(
        initialAmount = BigDecimal(10000),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        showSymbol = false,
        suffix = {
            Text(text = "원")
        },
    )
}

@Composable
fun InfoText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CustomViewTheme {
        Column {
            CurrencyTextFieldScreen()
        }
    }
}
