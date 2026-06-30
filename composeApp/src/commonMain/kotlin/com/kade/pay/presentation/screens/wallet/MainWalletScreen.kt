package com.kade.pay.presentation.screens.wallet

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kade.pay.presentation.screens.wallet.bitcoin.BitcoinNewWalletScreen
import com.kade.pay.presentation.screens.wallet.bitcoin.BitcoinNoWalletScreen
import com.kade.pay.presentation.screens.wallet.bitcoin.BitcoinWalletScreen
import com.kade.pay.presentation.screens.wallet.bitcoin.MNEMONIC_CONFIRM
import com.kade.pay.presentation.screens.wallet.bitcoin.NEW_WALLET
import com.kade.pay.presentation.screens.wallet.bitcoin.NO_WALLET
import com.kade.pay.presentation.screens.wallet.bitcoin.WALLET
import com.kade.pay.presentation.viewmodels.WalletViewModel

@Composable
fun MainWalletScreen(walletViewModel: WalletViewModel) {
    val navController = rememberNavController()
    NavHost(navController, if (walletViewModel.state.isWalletAvailable) WALLET else NO_WALLET) {
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
