package com.kade.pay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.kade
import org.jetbrains.compose.resources.painterResource
import java.awt.Dimension

fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KadePay",
            icon = painterResource(Res.drawable.kade),
        ) {
            SetMinimumSize(1024.dp, 800.dp)
            App()
        }
    }

@Composable
fun FrameWindowScope.SetMinimumSize(
    width: Dp,
    height: Dp,
) {
    val density = LocalDensity.current
    LaunchedEffect(density) {
        window.minimumSize =
            Dimension(
                (width.value * density.density).toInt(),
                (height.value * density.density).toInt(),
            )
    }
}
