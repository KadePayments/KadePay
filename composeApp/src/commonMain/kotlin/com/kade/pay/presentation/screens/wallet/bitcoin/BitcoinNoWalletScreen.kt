package com.kade.pay.presentation.screens.wallet.bitcoin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kade.pay.presentation.theme.KadePayTheme
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.import
import kadepay.composeapp.generated.resources.new
import kadepay.composeapp.generated.resources.wallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BitcoinNoWalletScreen() {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        FilledTonalButton(
            onClick = {},
        ) {
            Icon(painterResource(Res.drawable.wallet), null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(Res.string.new))
        }
        Spacer(Modifier.height(18.dp))
        FilledTonalButton(
            onClick = {},
        ) {
            Icon(painterResource(Res.drawable.wallet), null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(Res.string.import))
        }
    }
}

@Preview
@Composable
fun BitcoinNoWalletScreenPreview() {
    KadePayTheme {
        BitcoinNoWalletScreen()
    }
}
