package com.kade.pay.core.wallet

interface Wallet {
    val masterPubKey: String
    val descriptor: String
    val lastUsedIndex: Int
    val onChain: Boolean

    fun fingerprint(): String
}
