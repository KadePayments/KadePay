package com.kade.pay.presentation.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.kade.pay.core.data.storage.SecureStorage
import com.kade.pay.core.data.storage.getSecureStorage
import com.kade.pay.presentation.mapSaver
import com.kade.pay.presentation.viewmodels.WalletState
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.confirm_mnemonic_code
import kadepay.composeapp.generated.resources.finish
import kadepay.composeapp.generated.resources.wallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MnemonicsReorderView(
    walletState: WalletState,
    rows: Int,
    onFinish: (SecureStorage?) -> Unit,
) {
    val secureStorage = walletState.passphrase?.let { getSecureStorage(walletState.passphrase) }
    var isMnemonicsCorrect by rememberSaveable { mutableStateOf(false) }
    val correctRowMnemonicsFlags by rememberSaveable(stateSaver = mapSaver) {
        mutableStateOf(mutableMapOf())
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(
                stringResource(Res.string.confirm_mnemonic_code),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(32.dp))
        }
        items(rows) { row ->
            val mnemonics = walletState.mnemonics
            require(mnemonics.size % rows == 0) {
                "Mnemonics size: ${mnemonics.size} must be divisible by rows: $rows"
            }
            val wordsPerRow = mnemonics.size / rows
            val start = row * wordsPerRow
            val end = start + wordsPerRow
            val rowMnemonics = mnemonics.subList(start, end)

            val shuffledRowMnemonics = remember(rowMnemonics) { rowMnemonics.shuffled() }
            var reorderedRowMnemonics by remember { mutableStateOf(listOf<String>()) }

            fun checkMnemonics() {
                correctRowMnemonicsFlags[row] = reorderedRowMnemonics == rowMnemonics
                isMnemonicsCorrect = correctRowMnemonicsFlags.size == rows && correctRowMnemonicsFlags.all { it.value }
            }

            MnemonicsReorderRow(row + 1, shuffledRowMnemonics) {
                reorderedRowMnemonics = it
                checkMnemonics()
            }
        }
        item {
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
    }
}
