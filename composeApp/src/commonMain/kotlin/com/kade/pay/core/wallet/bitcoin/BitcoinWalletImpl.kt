package com.kade.pay.core.wallet.bitcoin

class BitcoinWalletImpl(
    override val descriptor: String,
    override val mnemonic: String,
    override val lastUsedIndex: Int,
    override val onChain: Boolean = true,
) : BitcoinWallet
