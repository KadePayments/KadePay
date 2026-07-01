package com.kade.pay.core.wallet

class WalletImpl(
    override val masterPubKey: String,
    override val descriptor: String,
    override val lastUsedIndex: Int,
) : Wallet {
    override fun fingerprint(): String = descriptor.substringAfter("tr([").substringBefore("/")
}
