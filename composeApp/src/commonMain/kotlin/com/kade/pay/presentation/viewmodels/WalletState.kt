package com.kade.pay.presentation.viewmodels

import com.kade.pay.core.wallet.Network

data class WalletState(
    val balance: Double = 00.00,
    val passphrase: String? = null,
    val mnemonics: List<String> = emptyList(),
    val isWalletAvailable: Boolean = false,
    val network: Network = Network.TESTNET,
    val errorMessage: String? = null,
)
