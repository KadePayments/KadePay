package com.kade.pay

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.kade
import org.jetbrains.compose.resources.painterResource

fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KadePay",
            icon = painterResource(Res.drawable.kade),
        ) {
            App()
        }
    }
