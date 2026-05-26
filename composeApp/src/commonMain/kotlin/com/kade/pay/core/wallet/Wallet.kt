package com.kade.pay.core.wallet

interface Wallet {
    val descriptor: String
    val mnemonic: String
    val lastUsedIndex: Int

    val onChain: Boolean
}
