package com.kade.pay.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kade.pay.core.wallet.Confirmation
import com.kade.pay.presentation.theme.KadePayTheme
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.confirmation
import kadepay.composeapp.generated.resources.fast
import kadepay.composeapp.generated.resources.keyboard_arrow_down
import kadepay.composeapp.generated.resources.keyboard_arrow_up
import kadepay.composeapp.generated.resources.moderate
import kadepay.composeapp.generated.resources.payment_settings
import kadepay.composeapp.generated.resources.solid
import kadepay.composeapp.generated.resources.sync_arrow_up
import kadepay.composeapp.generated.resources.sync_server
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PaymentSettingsScreen(onSyncServer: (Confirmation) -> Unit) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var expandMenu by rememberSaveable { mutableStateOf(false) }
            var confirmation by rememberSaveable { mutableStateOf(Confirmation.MODERATE) }
            val moderateConfirmation = stringResource(Res.string.moderate)
            var confirmationName by rememberSaveable { mutableStateOf(moderateConfirmation) }
            Text(
                stringResource(Res.string.payment_settings),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(Modifier.height(32.dp))
            Column {
                Box {
                    TextField(
                        confirmationName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(Res.string.confirmation)) },
                        trailingIcon = {
                            Icon(
                                painterResource(
                                    if (!expandMenu) {
                                        Res.drawable.keyboard_arrow_down
                                    } else {
                                        Res.drawable.keyboard_arrow_up
                                    },
                                ),
                                null,
                            )
                        },
                    )
                    Box(
                        Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { expandMenu = true },
                    )
                }
                DropdownMenu(
                    expandMenu,
                    { expandMenu = false },
                ) {
                    val fast = stringResource(Res.string.fast)
                    val moderate = stringResource(Res.string.moderate)
                    val solid = stringResource(Res.string.solid)
                    DropdownMenuItem(
                        {
                            Text(
                                fast,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        },
                        onClick = {
                            confirmationName = fast
                            confirmation = Confirmation.FAST
                            expandMenu = false
                        },
                    )
                    DropdownMenuItem(
                        {
                            Text(
                                moderate,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        },
                        onClick = {
                            confirmationName = moderate
                            confirmation = Confirmation.MODERATE
                            expandMenu = false
                        },
                    )
                    DropdownMenuItem(
                        {
                            Text(
                                solid,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        },
                        onClick = {
                            confirmationName = solid
                            confirmation = Confirmation.SOLID
                            expandMenu = false
                        },
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = { onSyncServer(confirmation) },
            ) {
                Icon(
                    painterResource(Res.drawable.sync_arrow_up),
                    null,
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.sync_server))
            }
        }
    }
}

@Preview
@Composable
fun PaymentSettingsScreenPreview() {
    KadePayTheme {
        PaymentSettingsScreen(
            onSyncServer = {},
        )
    }
}
