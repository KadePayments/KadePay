package com.kade.pay.presentation.viewmodels

import com.kade.pay.core.data.models.Utxo
import com.kade.pay.core.wallet.Network
import com.kade.pay.network.Config

data class WalletState(
    val isLoading: Boolean = true,
    val balance: Double = 00.00,
    val passphrase: String? = null,
    val mnemonics: List<String> = emptyList(),
    val pubKey: String? = null,
    val walletDescriptor: String? = null,
    val isWalletAvailable: Boolean = false,
    val network: Network = Network.REGTEST,
    val errorMessage: String? = null,
    val utxos: List<Utxo> = emptyList(),
    val config: Config = Config.REGTEST,
)
