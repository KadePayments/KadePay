package com.kade.pay.core.data.storage

import androidx.room.RoomDatabase
import com.kade.pay.core.data.db.Database
import com.kade.pay.core.data.db.entities.WalletEntity
import com.kade.pay.core.data.db.getDatabase

class WalletStorageImpl(
    dbBuilder: RoomDatabase.Builder<Database>,
) : WalletStorage {
    val db = dbBuilder.getDatabase()
    val walletDao = db.walletDao()

    override suspend fun save(wallet: WalletEntity) = walletDao.save(wallet)

    override suspend fun getAll(): List<WalletEntity> = walletDao.getAll()

    override suspend fun delete(wallet: WalletEntity) = walletDao.delete(wallet)
}
