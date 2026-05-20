package com.kade.pay.presentation.screens.wallet.bitcoin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kade.pay.presentation.theme.KadePayTheme
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.arrow_outward
import kadepay.composeapp.generated.resources.receive
import kadepay.composeapp.generated.resources.send
import kadepay.composeapp.generated.resources.visibility_off
import kadepay.composeapp.generated.resources.wallet
import kadepay.composeapp.generated.resources.your_keys
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BitcoinWalletScreen() {
    Box(contentAlignment = Alignment.Center) {
        Column(
            Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        ) {
            Row(Modifier.padding(start = 64.dp, top = 128.dp)) {
                Text(
                    "₿123",
                    color = MaterialTheme.colorScheme.onBackground,
                    style =
                        MaterialTheme.typography.headlineLarge
                            .copy(
                                fontWeight = FontWeight.Bold,
                            ),
                )
                Spacer(Modifier.width(12.dp))
                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        painterResource(Res.drawable.visibility_off),
                        null,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = {},
                Modifier.padding(start = 64.dp),
            ) {
                Icon(painterResource(Res.drawable.wallet), null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.your_keys))
            }
            Spacer(Modifier.height(64.dp))
        }
        Row {
            FilledTonalButton(
                onClick = {},
            ) {
                Icon(
                    painterResource(Res.drawable.arrow_outward),
                    null,
                    Modifier.rotate(180F),
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.receive))
            }
            Spacer(Modifier.width(16.dp))
            FilledTonalButton(
                onClick = {},
            ) {
                Icon(painterResource(Res.drawable.arrow_outward), null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.send))
            }
        }
    }
}

@Preview
@Composable
fun BitcoinWalletScreenPreview() {
    KadePayTheme {
        BitcoinWalletScreen()
    }
}
