package com.kade.pay.presentation.screens.wallet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kade.pay.core.data.models.BTC
import com.kade.pay.core.data.storage.getSecureStorage
import com.kade.pay.presentation.theme.KadePayTheme
import com.kade.pay.presentation.viewmodels.WalletState
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.addresses
import kadepay.composeapp.generated.resources.arrow_outward
import kadepay.composeapp.generated.resources.hide
import kadepay.composeapp.generated.resources.passphrase
import kadepay.composeapp.generated.resources.receive
import kadepay.composeapp.generated.resources.send
import kadepay.composeapp.generated.resources.show
import kadepay.composeapp.generated.resources.visibility_off
import kadepay.composeapp.generated.resources.visibility_on
import kadepay.composeapp.generated.resources.wallet
import kadepay.composeapp.generated.resources.your_keys
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    walletState: WalletState,
    onShowKeys: () -> Unit = {},
    onClearKeys: () -> Unit = {},
) {
    var showBalance by rememberSaveable { mutableStateOf(false) }
    val unit = "₿"
    val hiddenBalance =
        remember(walletState.balance) {
            PasswordVisualTransformation().filter(AnnotatedString("$unit${walletState.balance}"))
        }
    var showKeysView by rememberSaveable { mutableStateOf(false) }

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
                onClick = {
                    showKeysView = true
                },
                Modifier.padding(start = 64.dp),
            ) {
                Icon(painterResource(Res.drawable.wallet), stringResource(Res.string.your_keys))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.your_keys))
            }
            Spacer(Modifier.height(64.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
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
                    Icon(
                        painterResource(Res.drawable.arrow_outward),
                        stringResource(Res.string.send),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(Res.string.send))
                }
            }
            if (walletState.utxos.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(start = 64.dp, end = 64.dp, top = 16.dp, bottom = 16.dp),
                ) {
                    stickyHeader {
                        Text(
                            stringResource(Res.string.addresses),
                            Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        )
                        Spacer(
                            Modifier
                                .height(16.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background),
                        )
                    }
                    items(walletState.utxos) { utxo ->
                        Column(Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                // UI here, to be determined by utxo confirmation and/or invoice status
                                Checkbox(true, {}, Modifier.padding(0.dp))
                                /*RadioButton(true, onClick = {})
                            IconButton({}) {
                                Icon(
                                    painterResource(Res.drawable.schedule),
                                    stringResource(Res.string.waiting_confirmation),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }*/
                                Column {
                                    Text(
                                        utxo.address,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Text(
                                        "InvoiceId: ${utxo.invoiceId}",
                                        color = MaterialTheme.colorScheme.onBackground,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                                Spacer(Modifier.weight(1f))
                                Text(
                                    "${BTC}${utxo.amount}",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1,
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            HorizontalDivider(thickness = 2.dp)
                        }
                    }
                }
            }
        }
        AnimatedVisibility(showKeysView) {
            var showKeys by rememberSaveable { mutableStateOf(false) }
            var showPassInput by rememberSaveable { mutableStateOf(false) }
            LaunchedEffect(showKeysView) {
                onShowKeys()
            }
            ModalBottomSheet(
                {
                    onClearKeys()
                    showKeysView = false
                },
                Modifier.fillMaxWidth(),
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(start = 64.dp, end = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val transformation = PasswordVisualTransformation()
                    if (walletState.pubKey == null) return@ModalBottomSheet
                    var passphrase by remember { mutableStateOf("") }
                    val hiddenPubKey =
                        remember {
                            transformation.filter(
                                AnnotatedString(
                                    walletState.pubKey,
                                ),
                            )
                        }
                    val privKey: MutableState<String?> = remember { mutableStateOf(null) }
                    val pubKey =
                        buildAnnotatedString {
                            val text = "PublicKey: "
                            append(text)
                            addStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                ),
                                0,
                                text.length,
                            )
                            append(
                                if (showKeys) {
                                    walletState.pubKey
                                } else {
                                    hiddenPubKey.text
                                },
                            )
                        }
                    val privateKey =
                        buildAnnotatedString {
                            val text = "PrivateKey: "
                            append(text)
                            addStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                ),
                                0,
                                text.length,
                            )
                            append(privKey.value)
                        }

                    val secureStorage = getSecureStorage(passphrase)
                    LaunchedEffect(showKeysView, passphrase) {
                        if (walletState.walletDescriptor != null) {
                            privKey.value = secureStorage.get(walletState.walletDescriptor)
                        }
                        showKeys = privKey.value != null
                        if (passphrase.isBlank()) {
                            showPassInput = privKey.value == null
                        }
                    }

                    if (showPassInput) {
                        TextField(
                            passphrase,
                            onValueChange = {
                                passphrase = it
                            },
                            label = { Text(stringResource(Res.string.passphrase)) },
                            maxLines = 1,
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        pubKey,
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Spacer(Modifier.height(12.dp))
                    if (showKeys) {
                        Text(
                            privateKey,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                Spacer(Modifier.height(32.dp))
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
