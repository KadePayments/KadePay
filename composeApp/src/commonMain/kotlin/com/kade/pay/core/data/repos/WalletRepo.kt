package com.kade.pay.core.data.repos

import com.kade.pay.core.wallet.Wallet

interface WalletRepo {
    suspend fun save(wallet: Wallet)

    suspend fun updateWalletId(
        masterPubKey: String,
        walletId: String,
    )

    suspend fun getAll(): List<Wallet>

    suspend fun delete(wallet: Wallet)
}
