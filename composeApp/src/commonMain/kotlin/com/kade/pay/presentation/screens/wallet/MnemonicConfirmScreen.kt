package com.kade.pay.presentation.screens.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kade.pay.core.data.storage.SecureStorage
import com.kade.pay.core.data.storage.getSecureStorage
import com.kade.pay.presentation.theme.KadePayTheme
import com.kade.pay.presentation.viewmodels.WalletState
import com.kade.pay.presentation.views.MnemonicsReorderRow
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.arrow_back
import kadepay.composeapp.generated.resources.back
import kadepay.composeapp.generated.resources.confirm_mnemonic_code
import kadepay.composeapp.generated.resources.finish
import kadepay.composeapp.generated.resources.wallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MnemonicConfirmScreen(
    walletState: WalletState,
    onFinish: (SecureStorage?) -> Unit = { _ -> },
    onBack: () -> Unit = {},
) {
    var isMnemonicsCorrect by rememberSaveable { mutableStateOf(false) }
    val secureStorage = walletState.passphrase?.let { getSecureStorage(walletState.passphrase) }

    val firstRow = walletState.mnemonics.take(6)
    val secondRow =
        walletState.mnemonics
            .drop(6)
            .take(6)
    val thirdRow =
        walletState.mnemonics
            .drop(12)
            .take(6)
    val fourthRow =
        walletState.mnemonics
            .drop(18)
            .take(6)

    val shuffledFirstRow = rememberSaveable(firstRow) { firstRow.shuffled() }
    val shuffledSecondRow = rememberSaveable(firstRow) { secondRow.shuffled() }
    val shuffledThirdRow = rememberSaveable(firstRow) { thirdRow.shuffled() }
    val shuffledFourthRow = rememberSaveable(firstRow) { fourthRow.shuffled() }

    var reorderedFirstRow by rememberSaveable { mutableStateOf(listOf<String>()) }
    var reorderedSecondRow by rememberSaveable { mutableStateOf(listOf<String>()) }
    var reorderedThirdRow by rememberSaveable { mutableStateOf(listOf<String>()) }
    var reorderedFourthRow by rememberSaveable { mutableStateOf(listOf<String>()) }

    fun checkMnemonics() {
        isMnemonicsCorrect =
            reorderedFirstRow == firstRow &&
            reorderedSecondRow == secondRow &&
            reorderedThirdRow == thirdRow &&
            reorderedFourthRow == fourthRow
    }

    Box {
        Column(
            Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                stringResource(Res.string.confirm_mnemonic_code),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(32.dp))
            if (walletState.mnemonics.isNotEmpty()) {
                Column(
                    Modifier.padding(start = 24.dp, end = 24.dp),
                ) {
                    MnemonicsReorderRow(1, shuffledFirstRow) {
                        reorderedFirstRow = it
                        checkMnemonics()
                    }
                    MnemonicsReorderRow(2, shuffledSecondRow) {
                        reorderedSecondRow = it
                        checkMnemonics()
                    }
                    MnemonicsReorderRow(3, shuffledThirdRow) {
                        reorderedThirdRow = it
                        checkMnemonics()
                    }
                    MnemonicsReorderRow(4, shuffledFourthRow) {
                        reorderedFourthRow = it
                        checkMnemonics()
                    }
                }
            }
            Spacer(Modifier.height(64.dp))
            FilledTonalButton(
                onClick = {
                    onFinish(secureStorage)
                },
                enabled = isMnemonicsCorrect,
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
fun PreviewMnemonicConfirmScreen() {
    KadePayTheme {
        MnemonicConfirmScreen(WalletState())
    }
}
