package com.kade.pay.presentation.viewmodels

import com.kade.pay.core.wallet.Network

data class WalletState(
    val balance: Double = 123.57,
    val mnemonics: List<String> = listOf(),
    val onChainWalletAvailable: Boolean = false,
    val onChainNetwork: Network = Network.TESTNET,
    val errorMessage: String? = null,
)
