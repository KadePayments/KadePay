package com.kade.pay.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kade.pay.core.data.db.getDatabaseBuilder
import com.kade.pay.presentation.screens.wallet.MnemonicConfirmScreen
import com.kade.pay.presentation.screens.wallet.bitcoin.BitcoinNewWalletScreen
import com.kade.pay.presentation.screens.wallet.bitcoin.BitcoinNoWalletScreen
import com.kade.pay.presentation.screens.wallet.bitcoin.BitcoinWalletScreen
import com.kade.pay.presentation.screens.wallet.bitcoin.MNEMONIC_CONFIRM
import com.kade.pay.presentation.screens.wallet.bitcoin.NEW_WALLET
import com.kade.pay.presentation.screens.wallet.bitcoin.NO_WALLET
import com.kade.pay.presentation.screens.wallet.bitcoin.WALLET
import com.kade.pay.presentation.theme.KadePayTheme
import com.kade.pay.presentation.theme.arkadeIconColor
import com.kade.pay.presentation.theme.bitcoinIconColor
import com.kade.pay.presentation.viewmodels.WalletViewModel
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.app_name
import kadepay.composeapp.generated.resources.arkade
import kadepay.composeapp.generated.resources.bitcoin
import kadepay.composeapp.generated.resources.btc
import kadepay.composeapp.generated.resources.btc_logo
import kadepay.composeapp.generated.resources.invoice
import kadepay.composeapp.generated.resources.invoices
import kadepay.composeapp.generated.resources.kade
import kadepay.composeapp.generated.resources.paybutton
import kadepay.composeapp.generated.resources.payments
import kadepay.composeapp.generated.resources.wallets
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen() {
    val dbBuilder = getDatabaseBuilder()
    val walletViewModel = viewModel { WalletViewModel(dbBuilder) }
    var selectedNavItem: SelectedNavItem by rememberSaveable(stateSaver = navItemStateSaver) {
        mutableStateOf(SelectedNavItem.Bitcoin)
    }
    val containerWidth = LocalWindowInfo.current.containerDpSize.width

    if (containerWidth < 600.dp) {
        Box {
            when (selectedNavItem) {
                is SelectedNavItem.Bitcoin -> {
                    val navController = rememberNavController()
                    LaunchedEffect(Unit) {
                        walletViewModel.onLoadWallets()
                    }
                    val onChainWalletAvailable = walletViewModel.state.onChainWalletAvailable
                    LaunchedEffect(onChainWalletAvailable) {
                        if (onChainWalletAvailable) {
                            navController.navigate(WALLET) {
                                popUpTo(NO_WALLET) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }

                    NavHost(navController, NO_WALLET) {
                        composable(NO_WALLET) {
                            BitcoinNoWalletScreen(
                                onNew = {
                                    navController.navigate(NEW_WALLET) {
                                        launchSingleTop = true
                                    }
                                },
                            )
                        }
                        composable(NEW_WALLET) {
                            BitcoinNewWalletScreen(
                                walletViewModel.state,
                                onLaunch = {
                                    walletViewModel.onNewWallet(true)
                                },
                                onContinue = { passphrase ->
                                    walletViewModel.updatePassphrase(passphrase)
                                    navController.navigate(MNEMONIC_CONFIRM) {
                                        popUpTo(NEW_WALLET)
                                        launchSingleTop = true
                                    }
                                },
                                onBack = {
                                    navController.popBackStack()
                                },
                            )
                        }
                        composable(MNEMONIC_CONFIRM) {
                            MnemonicConfirmScreen(
                                walletViewModel.state,
                                onFinish = { secureStorage ->
                                    walletViewModel.onCreateWallet(
                                        true,
                                        secureStorage,
                                    ) {
                                        navController.navigate(WALLET) {
                                            popUpTo(MNEMONIC_CONFIRM) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                },
                                onBack = { navController.popBackStack() },
                            )
                        }
                        composable(WALLET) {
                            BitcoinWalletScreen(walletViewModel.state)
                        }
                    }
                }
                is SelectedNavItem.Arkade -> {}
                is SelectedNavItem.Invoices -> {}
                is SelectedNavItem.PayButton -> {}
            }
        }
    } else {
        Row(
            Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        ) {
            NavigationRail(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                header = {
                    Row(
                        Modifier.padding(
                            top = 48.dp,
                            start = 24.dp,
                            end = 24.dp,
                        ),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Icon(
                            painterResource(Res.drawable.kade),
                            null,
                            Modifier.padding(bottom = 5.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )

                        Text(
                            stringResource(Res.string.app_name),
                            Modifier.padding(start = 4.dp),
                            style =
                                MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                ),
                        )
                    }
                },
            ) {
                Spacer(Modifier.height(48.dp))
                Column {
                    Text(
                        stringResource(Res.string.wallets),
                        style =
                            MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                    )
                    Spacer(Modifier.height(12.dp))
                    NavigationRailItem(
                        selected = selectedNavItem == SelectedNavItem.Bitcoin,
                        onClick = {
                            selectedNavItem = SelectedNavItem.Bitcoin
                        },
                        icon = {
                            Icon(
                                painterResource(Res.drawable.btc),
                                null,
                                tint = bitcoinIconColor,
                            )
                        },
                        label = { Text(stringResource(Res.string.bitcoin)) },
                    )
                    Spacer(Modifier.height(12.dp))
                    NavigationRailItem(
                        selected = selectedNavItem == SelectedNavItem.Arkade,
                        onClick = {
                            selectedNavItem = SelectedNavItem.Arkade
                        },
                        icon = {
                            Icon(
                                painterResource(Res.drawable.arkade),
                                null,
                                tint = arkadeIconColor,
                            )
                        },
                        label = { Text(stringResource(Res.string.arkade)) },
                    )
                }
                Spacer(Modifier.height(24.dp))
                Column {
                    Text(
                        stringResource(Res.string.payments),
                        style =
                            MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                    )
                    Spacer(Modifier.height(12.dp))
                    NavigationRailItem(
                        selected = selectedNavItem == SelectedNavItem.Invoices,
                        onClick = {
                            selectedNavItem = SelectedNavItem.Invoices
                        },
                        icon = {
                            Image(
                                painterResource(Res.drawable.invoice),
                                null,
                            )
                        },
                        label = { Text(stringResource(Res.string.invoices)) },
                    )
                    Spacer(Modifier.height(12.dp))
                    NavigationRailItem(
                        selected = selectedNavItem == SelectedNavItem.PayButton,
                        onClick = {
                            selectedNavItem = SelectedNavItem.PayButton
                        },
                        icon = {
                            Image(
                                painterResource(Res.drawable.btc_logo),
                                null,
                            )
                        },
                        label = { Text(stringResource(Res.string.paybutton)) },
                    )
                }
            }
            when (selectedNavItem) {
                is SelectedNavItem.Bitcoin -> {
                    val navController = rememberNavController()
                    LaunchedEffect(Unit) {
                        walletViewModel.onLoadWallets()
                    }
                    val onChainWalletAvailable = walletViewModel.state.onChainWalletAvailable
                    LaunchedEffect(onChainWalletAvailable) {
                        if (onChainWalletAvailable) {
                            navController.navigate(WALLET) {
                                popUpTo(NO_WALLET) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }

                    NavHost(navController, NO_WALLET) {
                        composable(NO_WALLET) {
                            BitcoinNoWalletScreen(
                                onNew = {
                                    navController.navigate(NEW_WALLET) {
                                        launchSingleTop = true
                                    }
                                },
                            )
                        }
                        composable(NEW_WALLET) {
                            BitcoinNewWalletScreen(
                                walletViewModel.state,
                                onLaunch = {
                                    walletViewModel.onNewWallet(true)
                                },
                                onContinue = { passphrase ->
                                    walletViewModel.updatePassphrase(passphrase)
                                    navController.navigate(MNEMONIC_CONFIRM) {
                                        popUpTo(NEW_WALLET)
                                        launchSingleTop = true
                                    }
                                },
                                onBack = {
                                    navController.popBackStack()
                                },
                            )
                        }
                        composable(MNEMONIC_CONFIRM) {
                            MnemonicConfirmScreen(
                                walletViewModel.state,
                                onFinish = { secureStorage ->
                                    walletViewModel.onCreateWallet(
                                        true,
                                        secureStorage,
                                    ) {
                                        navController.navigate(WALLET) {
                                            popUpTo(MNEMONIC_CONFIRM) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                },
                                onBack = { navController.popBackStack() },
                            )
                        }
                        composable(WALLET) {
                            BitcoinWalletScreen(walletViewModel.state)
                        }
                    }
                }

                is SelectedNavItem.Arkade -> {}
                is SelectedNavItem.Invoices -> {}
                is SelectedNavItem.PayButton -> {}
            }
        }
    }
}

sealed class SelectedNavItem {
    object Bitcoin : SelectedNavItem()

    object Arkade : SelectedNavItem()

    object Invoices : SelectedNavItem()

    object PayButton : SelectedNavItem()
}

private val navItemStateSaver =
    Saver<SelectedNavItem, Int>(
        {
            when (it) {
                SelectedNavItem.Bitcoin -> 0
                SelectedNavItem.Arkade -> 1
                SelectedNavItem.Invoices -> 2
                SelectedNavItem.PayButton -> 3
            }
        },
        {
            when (it) {
                0 -> SelectedNavItem.Bitcoin
                1 -> SelectedNavItem.Arkade
                2 -> SelectedNavItem.Invoices
                3 -> SelectedNavItem.PayButton
                else -> throw IllegalArgumentException("Invalid index")
            }
        },
    )

@Preview
@Composable
fun MainScreenPreview() {
    KadePayTheme {
        MainScreen()
    }
}
