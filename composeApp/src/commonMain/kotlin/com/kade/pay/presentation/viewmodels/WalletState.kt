package com.kade.pay.presentation.viewmodels

import com.kade.pay.core.wallet.Network

data class WalletState(
    val mnemonics: List<String> = listOf(),
    val onChainWalletAvailable: Boolean = false,
    val onChainNetwork: Network = Network.TESTNET,
)
