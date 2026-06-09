package com.kade.pay.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.continue_to_confirm
import kadepay.composeapp.generated.resources.info
import kadepay.composeapp.generated.resources.mnemonic_warn
import kadepay.composeapp.generated.resources.passphrase
import kadepay.composeapp.generated.resources.wallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MnemonicsView(
    mnemonics: List<String>,
    rows: Int,
    onContinue: (String) -> Unit = {},
) {
    var passphrase by rememberSaveable { mutableStateOf("") }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        item {
            Spacer(Modifier.height(96.dp))
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
        }
        items(rows) { row ->
            val wordsPerRow = mnemonics.size / rows
            val start = row * wordsPerRow
            val end = start + wordsPerRow
            val rowMnemonics = mnemonics.subList(start, end)
            LazyRow(
                Modifier.padding(start = 24.dp, end = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                item {
                    Text(
                        "${row + 1}.",
                        style =
                            LocalTextStyle.current.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                            ),
                    )
                }
                items(rowMnemonics) {
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
        item {
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
        item {
            Spacer(Modifier.height(48.dp))
            FilledTonalButton(
                onClick = {
                    onContinue(passphrase)
                },
            ) {
                Icon(painterResource(Res.drawable.wallet), null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.continue_to_confirm))
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}
