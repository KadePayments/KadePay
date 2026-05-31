package com.kade.pay.presentation.screens.wallet.bitcoin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kade.pay.core.data.db.getDatabaseBuilder
import com.kade.pay.core.data.storage.getSecureStorage
import com.kade.pay.presentation.theme.KadePayTheme
import com.kade.pay.presentation.viewmodels.WalletViewModel
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.arrow_back
import kadepay.composeapp.generated.resources.back
import kadepay.composeapp.generated.resources.finish
import kadepay.composeapp.generated.resources.info
import kadepay.composeapp.generated.resources.mnemonic_warn
import kadepay.composeapp.generated.resources.passphrase
import kadepay.composeapp.generated.resources.wallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BitcoinNewWalletScreen(
    onNavigate: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    var passphrase by rememberSaveable {
        mutableStateOf("")
    }
    val dbBuilder = getDatabaseBuilder()
    val viewModel: WalletViewModel =
        viewModel {
            WalletViewModel(dbBuilder)
        }
    val secureStorage = getSecureStorage(passphrase)

    LaunchedEffect(Unit) {
        viewModel.onNewWallet(true)
    }

    Box {
        Column(
            Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            TextField(
                passphrase,
                onValueChange = {
                    passphrase = it
                },
                label = { Text(stringResource(Res.string.passphrase)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            Spacer(Modifier.height(32.dp))
            if (viewModel.state.mnemonics.isNotEmpty()) {
                Column(
                    Modifier.padding(start = 24.dp, end = 24.dp),
                ) {
                    val firstRow = viewModel.state.mnemonics.take(6)
                    val secondRow =
                        viewModel.state.mnemonics
                            .drop(6)
                            .take(6)
                    val thirdRow =
                        viewModel.state.mnemonics
                            .drop(12)
                            .take(6)
                    val fourthRow =
                        viewModel.state.mnemonics
                            .drop(18)
                            .take(6)
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        item {
                            Text(
                                "1.",
                                style =
                                    LocalTextStyle.current.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    ),
                            )
                        }
                        items(firstRow) {
                            Spacer(Modifier.width(12.dp))
                            SuggestionChip(
                                onClick = {},
                                {
                                    Text(it)
                                },
                            )
                        }
                    }
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        item {
                            Text(
                                "2.",
                                style =
                                    LocalTextStyle.current.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    ),
                            )
                        }
                        items(secondRow) {
                            Spacer(Modifier.width(12.dp))
                            SuggestionChip(
                                onClick = {},
                                {
                                    Text(it)
                                },
                            )
                        }
                    }
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        item {
                            Text(
                                "3.",
                                style =
                                    LocalTextStyle.current.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    ),
                            )
                        }
                        items(thirdRow) {
                            Spacer(Modifier.width(12.dp))
                            SuggestionChip(
                                onClick = {},
                                {
                                    Text(it)
                                },
                            )
                        }
                    }
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        item {
                            Text(
                                "4.",
                                style =
                                    LocalTextStyle.current.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    ),
                            )
                        }
                        items(fourthRow) {
                            Spacer(Modifier.width(12.dp))
                            SuggestionChip(
                                onClick = {},
                                {
                                    Text(it)
                                },
                            )
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
                Row(Modifier.padding(start = 24.dp, end = 24.dp)) {
                    Icon(
                        painterResource(Res.drawable.info),
                        null,
                        tint = MaterialTheme.colorScheme.error,
                    )
                    Text(
                        stringResource(Res.string.mnemonic_warn),
                        Modifier.padding(start = 12.dp),
                        style =
                            LocalTextStyle.current.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                            ),
                    )
                }
            }
            Spacer(Modifier.height(64.dp))
            FilledTonalButton(
                onClick = {
                    viewModel.onCreateWallet(passphrase, true, secureStorage) {
                        onNavigate()
                    }
                },
            ) {
                Icon(painterResource(Res.drawable.wallet), null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.finish))
            }
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
fun BitcoinNewWalletScreenPreview() {
    KadePayTheme {
        BitcoinNewWalletScreen()
    }
}
