package com.kade.pay.core.wallet.bitcoin

import com.kade.pay.core.wallet.Wallet

interface BitcoinWallet : Wallet {
    companion object {
        fun new(passphrase: String): BitcoinWallet = BitcoinWalletImpl()

        fun import(): BitcoinWallet = BitcoinWalletImpl()
    }
}
