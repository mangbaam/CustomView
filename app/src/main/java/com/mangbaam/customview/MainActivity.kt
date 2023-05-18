package com.mangbaam.customview

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mangbaam.customview.ui.theme.CustomViewTheme

sealed interface Navigate {
    object CurrencyTextField : Navigate
    object BlurView : Navigate
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(Modifier.fillMaxSize()) {
                MainScreen {
                    val intent = when (it) {
                        Navigate.CurrencyTextField -> Intent(this, CurrencyTextFieldActivity::class.java)
                        Navigate.BlurView -> Intent(this, BlurViewActivity::class.java)
                    }
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun MainScreen(navigateTo: (Navigate) -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(onClick = {
            navigateTo(Navigate.CurrencyTextField)
        }) {
            Text(text = "CurrencyTextField")
        }
        Button(onClick = {
            navigateTo(Navigate.BlurView)
        }) {
            Text(text = "BlurView")
        }
    }
}

@Preview
@Composable
fun MainScreenPreView() {
    Surface(Modifier.fillMaxSize()) {
        CustomViewTheme {
            MainScreen()
        }
    }
}
