package com.kade.pay

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kade.pay.presentation.screens.MainScreen
import com.kade.pay.presentation.theme.KadePayTheme

@Composable
@Preview
fun App() {
    KadePayTheme(false) {
        MainScreen()
    }
}
