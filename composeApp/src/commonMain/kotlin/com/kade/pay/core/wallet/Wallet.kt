package com.kade.pay.core.wallet

interface Wallet {
    val masterPubKey: String
    val descriptor: String
    val lastUsedIndex: Int

    fun fingerprint(): String
}
