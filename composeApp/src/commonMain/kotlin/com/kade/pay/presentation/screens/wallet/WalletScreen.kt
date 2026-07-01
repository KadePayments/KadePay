package com.kade.pay.presentation.screens.wallet

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kade.pay.presentation.theme.KadePayTheme
import com.kade.pay.presentation.viewmodels.WalletState
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.arrow_outward
import kadepay.composeapp.generated.resources.hide
import kadepay.composeapp.generated.resources.receive
import kadepay.composeapp.generated.resources.send
import kadepay.composeapp.generated.resources.show
import kadepay.composeapp.generated.resources.visibility_off
import kadepay.composeapp.generated.resources.visibility_on
import kadepay.composeapp.generated.resources.wallet
import kadepay.composeapp.generated.resources.your_keys
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WalletScreen(walletState: WalletState) {
    var showBalance by rememberSaveable { mutableStateOf(false) }
    val unit = "₿"
    val hiddenBalance =
        remember(walletState.balance) {
            PasswordVisualTransformation().filter(AnnotatedString("$unit${walletState.balance}"))
        }
    Box(contentAlignment = Alignment.Center) {
        Column(
            Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        ) {
            Row(Modifier.padding(start = 64.dp, top = 128.dp)) {
                Text(
                    if (showBalance) "$unit${walletState.balance}" else hiddenBalance.text.text,
                    color = MaterialTheme.colorScheme.onBackground,
                    style =
                        MaterialTheme.typography.headlineLarge
                            .copy(
                                fontWeight = FontWeight.Bold,
                            ),
                )
                Spacer(Modifier.width(12.dp))
                IconButton(
                    onClick = {
                        showBalance = !showBalance
                    },
                ) {
                    Icon(
                        painterResource(if (showBalance) Res.drawable.visibility_off else Res.drawable.visibility_on),
                        stringResource(if (showBalance) Res.string.hide else Res.string.show),
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = {},
                Modifier.padding(start = 64.dp),
                enabled = false,
            ) {
                Icon(painterResource(Res.drawable.wallet), stringResource(Res.string.your_keys))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.your_keys))
            }
            Spacer(Modifier.height(64.dp))
        }
        Row {
            FilledTonalButton(
                onClick = {},
                enabled = false,
            ) {
                Icon(
                    painterResource(Res.drawable.arrow_outward),
                    stringResource(Res.string.receive),
                    Modifier.rotate(180F),
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.receive))
            }
            Spacer(Modifier.width(16.dp))
            FilledTonalButton(
                onClick = {},
                enabled = false,
            ) {
                Icon(painterResource(Res.drawable.arrow_outward), stringResource(Res.string.send))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.send))
            }
        }
    }
}

@Preview
@Composable
fun WalletScreenPreview() {
    KadePayTheme {
        WalletScreen(WalletState())
    }
}
