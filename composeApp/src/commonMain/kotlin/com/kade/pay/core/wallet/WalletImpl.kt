package com.kade.pay.core.wallet

import com.kade.pay.network.Config

class WalletImpl(
    override val masterPubKey: String,
    override val descriptor: String,
    override val lastUsedIndex: Int,
    override val config: Config = Config.RegTest,
) : Wallet {
    override var walletId: String? = null

    override fun fingerprint(): String = descriptor.substringAfter("tr([").substringBefore("/")

    override fun updateWalletId(walletId: String) {
        this.walletId = walletId
    }
}
