package com.kade.pay.core.data.repos

import androidx.room.RoomDatabase
import com.kade.pay.core.data.db.Database
import com.kade.pay.core.data.db.entities.WalletEntity
import com.kade.pay.core.data.storage.WalletStorageImpl
import com.kade.pay.core.wallet.Wallet

class WalletRepoImpl(
    dbBuilder: RoomDatabase.Builder<Database>,
) : WalletRepo {
    private val storage = WalletStorageImpl(dbBuilder)

    override suspend fun save(wallet: Wallet) {
        storage.save(WalletEntity.fromWallet(wallet))
    }

    override suspend fun getAll(): List<Wallet> = storage.getAll().map { it.toWallet() }

    override suspend fun delete(wallet: Wallet) {
        storage.delete(WalletEntity.fromWallet(wallet))
    }
}
