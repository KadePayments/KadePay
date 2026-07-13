package com.kade.pay.presentation.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material3.IconButton
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
import com.kade.pay.core.validateUrl
import com.kade.pay.core.wallet.Network
import com.kade.pay.network.Config
import com.kade.pay.presentation.theme.KadePayTheme
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.btc_logo
import kadepay.composeapp.generated.resources.btc_regtest
import kadepay.composeapp.generated.resources.btc_signet
import kadepay.composeapp.generated.resources.btc_testnet
import kadepay.composeapp.generated.resources.edit_server_url
import kadepay.composeapp.generated.resources.keyboard_arrow_down
import kadepay.composeapp.generated.resources.keyboard_arrow_up
import kadepay.composeapp.generated.resources.link
import kadepay.composeapp.generated.resources.network
import kadepay.composeapp.generated.resources.server_settings
import kadepay.composeapp.generated.resources.server_url
import kadepay.composeapp.generated.resources.sync_arrow_up
import kadepay.composeapp.generated.resources.sync_server
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ServerSettingsScreen(
    config: Config,
    onSyncServer: (String, Network) -> Unit,
) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val derivedNetwork = config.network
            var serverUrl by rememberSaveable { mutableStateOf(config.kadePayUrl) }
            var network by rememberSaveable { mutableStateOf(derivedNetwork) }
            var expandMenu by rememberSaveable { mutableStateOf(false) }
            Text(
                stringResource(Res.string.server_settings),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(Modifier.height(32.dp))
            var isReadOnlyServerUrl by rememberSaveable { mutableStateOf(true) }
            Column {
                TextField(
                    serverUrl,
                    onValueChange = {
                        serverUrl = it
                    },
                    readOnly = isReadOnlyServerUrl,
                    label = { Text(stringResource(Res.string.server_url)) },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = { isReadOnlyServerUrl = !isReadOnlyServerUrl },
                        ) {
                            Icon(
                                painterResource(Res.drawable.link),
                                stringResource(Res.string.edit_server_url),
                            )
                        }
                    },
                )
                Spacer(Modifier.height(12.dp))
                Box {
                    TextField(
                        network.name.lowercase(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(Res.string.network)) },
                        leadingIcon = {
                            val icon =
                                when (network) {
                                    Network.MAINNET -> painterResource(Res.drawable.btc_logo)
                                    Network.SIGNET -> painterResource(Res.drawable.btc_signet)
                                    Network.REGTEST -> painterResource(Res.drawable.btc_regtest)
                                    Network.TESTNET -> painterResource(Res.drawable.btc_testnet)
                                }
                            Image(icon, null)
                        },
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
                    DropdownMenuItem(
                        {
                            Text(
                                Network.MAINNET.name.lowercase(),
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        },
                        leadingIcon = {
                            Image(
                                painterResource(Res.drawable.btc_logo),
                                null,
                            )
                        },
                        onClick = {
                            network = Network.MAINNET
                            expandMenu = false
                            serverUrl = ""
                            isReadOnlyServerUrl = false
                        },
                    )
                    DropdownMenuItem(
                        {
                            Text(
                                Network.SIGNET.name.lowercase(),
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        },
                        leadingIcon = {
                            Image(
                                painterResource(Res.drawable.btc_signet),
                                null,
                            )
                        },
                        onClick = {
                            network = Network.SIGNET
                            expandMenu = false
                            serverUrl = ""
                            isReadOnlyServerUrl = false
                        },
                    )
                    DropdownMenuItem(
                        {
                            Text(
                                Network.REGTEST.name.lowercase(),
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        },
                        leadingIcon = {
                            Image(
                                painterResource(Res.drawable.btc_regtest),
                                null,
                            )
                        },
                        onClick = {
                            network = Network.REGTEST
                            expandMenu = false
                            serverUrl = Config.RegTest.kadePayUrl
                            isReadOnlyServerUrl = false
                        },
                    )
                    DropdownMenuItem(
                        {
                            Text(
                                Network.TESTNET.name.lowercase(),
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        },
                        leadingIcon = {
                            Image(
                                painterResource(Res.drawable.btc_testnet),
                                null,
                            )
                        },
                        onClick = {
                            network = Network.TESTNET
                            expandMenu = false
                            serverUrl = ""
                            isReadOnlyServerUrl = false
                        },
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = {
                    // Notify user to enter a valid URL
                    if (serverUrl.isEmpty()) {
                        return@Button
                    }
                    // Notify user to enter a valid URL
                    if (!validateUrl(serverUrl)) {
                        return@Button
                    }
                    onSyncServer(serverUrl, network)
                },
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
fun ServerSettingsScreenPreview() {
    KadePayTheme {
        ServerSettingsScreen(
            Config.RegTest,
            onSyncServer = { _, _ -> },
        )
    }
}
