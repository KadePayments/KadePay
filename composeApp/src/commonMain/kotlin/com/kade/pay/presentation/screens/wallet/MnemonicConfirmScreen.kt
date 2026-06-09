package com.kade.pay.presentation.screens.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kade.pay.core.data.storage.SecureStorage
import com.kade.pay.presentation.theme.KadePayTheme
import com.kade.pay.presentation.viewmodels.WalletState
import com.kade.pay.presentation.views.MnemonicsReorderView
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.arrow_back
import kadepay.composeapp.generated.resources.back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MnemonicConfirmScreen(
    walletState: WalletState,
    onFinish: (SecureStorage?) -> Unit = { _ -> },
    onBack: () -> Unit = {},
) {
    val containerSize = LocalWindowInfo.current.containerDpSize.width
    Box(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        if (walletState.mnemonics.isNotEmpty()) {
            MnemonicsReorderView(walletState, if (containerSize < 600.dp) 8 else 4, onFinish)
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
fun PreviewMnemonicConfirmScreen() {
    KadePayTheme {
        MnemonicConfirmScreen(WalletState())
    }
}
