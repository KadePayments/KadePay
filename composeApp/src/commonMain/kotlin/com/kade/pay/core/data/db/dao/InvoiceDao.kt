package com.kade.pay.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kade.pay.core.data.db.entities.InvoiceEntity

@Dao
interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(invoice: InvoiceEntity)

    @Query("SELECT * FROM invoices WHERE xPubKeyId = :walletId")
    suspend fun getAll(walletId: String): List<InvoiceEntity>
}
