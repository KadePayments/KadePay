package com.kade.pay.core.wallet.bitcoin

class BitcoinWalletImpl(
    override val masterPubKey: String,
    override val descriptor: String,
    override val mnemonic: String,
    override val lastUsedIndex: Int,
    override val onChain: Boolean = true,
) : BitcoinWallet {
    override fun fingerprint(): String = descriptor.substringAfter("tr([").substringBefore("/")
}
