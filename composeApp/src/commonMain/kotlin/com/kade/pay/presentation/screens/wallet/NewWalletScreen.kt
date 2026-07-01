package com.kade.pay.presentation.screens.wallet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kade.pay.presentation.theme.KadePayTheme
import com.kade.pay.presentation.viewmodels.WalletState
import com.kade.pay.presentation.views.MnemonicsView
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.arrow_back
import kadepay.composeapp.generated.resources.back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NewWalletScreen(
    walletState: WalletState,
    onLaunch: () -> Unit = {},
    onContinue: (String) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val containerWidth = LocalWindowInfo.current.containerDpSize.width

    LaunchedEffect(Unit) {
        onLaunch()
    }

    Box(
        Modifier.systemBarsPadding().fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (walletState.mnemonics.isNotEmpty()) {
            MnemonicsView(
                walletState.mnemonics,
                if (containerWidth < 600.dp) 8 else 4,
                onContinue,
            )
        }
        IconButton(
            onClick = onBack,
            Modifier.padding(16.dp).align(Alignment.TopStart),
        ) {
            Icon(
                painterResource(Res.drawable.arrow_back),
                stringResource(Res.string.back),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview
@Composable
fun NewWalletScreenPreview() {
    KadePayTheme {
        NewWalletScreen(WalletState())
    }
}
