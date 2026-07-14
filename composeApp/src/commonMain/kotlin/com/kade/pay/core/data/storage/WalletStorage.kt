package com.kade.pay.core.data.storage

import com.kade.pay.core.data.db.entities.WalletEntity

interface WalletStorage {
    suspend fun save(wallet: WalletEntity)

    suspend fun updateWalletId(
        masterPubKey: String,
        walletId: String,
    )

    suspend fun getAll(): List<WalletEntity>

    suspend fun delete(wallet: WalletEntity)
}
