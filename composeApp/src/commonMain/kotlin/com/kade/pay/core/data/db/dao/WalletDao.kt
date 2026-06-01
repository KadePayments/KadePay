package com.kade.pay.core.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.kade.pay.core.data.db.entities.WalletEntity

@Dao
interface WalletDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(wallet: WalletEntity)

    @Query("SELECT * FROM wallets")
    suspend fun getAll(): List<WalletEntity>

    @Delete
    suspend fun delete(wallet: WalletEntity)
}
