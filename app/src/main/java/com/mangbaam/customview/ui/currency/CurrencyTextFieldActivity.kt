package com.mangbaam.customview.ui.currency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mangbaam.currencytextfield.OutlinedCurrencyTextField
import com.mangbaam.customview.ui.theme.CustomViewTheme

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
}

@Composable
fun CurrencyTextFieldScreen() {
    val initValue = 1234567L

    var amount by remember { mutableStateOf(initValue) }
    var displayed by remember {
        mutableStateOf("")
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp),
    ) {
        Text(text = "표시된 값: $displayed")
        Text(text = "금액: $amount")
        OutlinedCurrencyTextField(
            initialAmount = initValue,
            onValueChanged = {
                amount = it
            },
            onTextChanged = {
                displayed = it
            },
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CurrencyTextFieldPreview() {
    CustomViewTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                CurrencyTextFieldScreen()
            }
        }
    }
}
