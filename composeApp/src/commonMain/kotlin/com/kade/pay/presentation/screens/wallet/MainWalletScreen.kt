package com.kade.pay.presentation.screens.wallet

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kade.pay.presentation.viewmodels.WalletViewModel

@Composable
fun MainWalletScreen(walletViewModel: WalletViewModel) {
    val navController = rememberNavController()
    NavHost(navController, if (walletViewModel.state.isWalletAvailable) WALLET else NO_WALLET) {
        composable(NO_WALLET) {
            NoWalletScreen(
                onNew = {
                    navController.navigate(NEW_WALLET) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(NEW_WALLET) {
            NewWalletScreen(
                walletViewModel.state,
                onLaunch = {
                    walletViewModel.onNewWallet()
                },
                onContinue = { passphrase ->
                    walletViewModel.updatePassphrase(passphrase)
                    navController.navigate(MNEMONIC_CONFIRM) {
                        popUpTo(NEW_WALLET)
                        launchSingleTop = true
                    }
                },
                onBack = {
                    walletViewModel.clearMnemonics()
                    navController.popBackStack()
                },
            )
        }
        composable(MNEMONIC_CONFIRM) {
            MnemonicConfirmScreen(
                walletViewModel.state,
                onFinish = { secureStorage ->
                    walletViewModel.onCreateWallet(
                        secureStorage,
                    ) {
                        navController.navigate(WALLET) {
                            popUpTo(NO_WALLET) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable(WALLET) {
            WalletScreen(
                walletViewModel.state,
                onShowKeys = walletViewModel::onShowKeys,
                onClearKeys = walletViewModel::onClearKeys,
            )
        }
    }
}
