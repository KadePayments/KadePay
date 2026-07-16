package com.kade.pay.core.data.storage

import androidx.room.RoomDatabase
import com.kade.pay.core.data.db.Database
import com.kade.pay.core.data.db.entities.InvoiceEntity
import com.kade.pay.core.data.db.getDatabase

class InvoiceStorageImpl(
    databaseBuilder: RoomDatabase.Builder<Database>,
) : InvoiceStorage {
    private val db = databaseBuilder.getDatabase()
    private val invoiceDao = db.invoiceDao()

    override suspend fun save(invoice: InvoiceEntity) = invoiceDao.save(invoice)

    override suspend fun save(invoices: List<InvoiceEntity>) = invoiceDao.save(invoices)

    override suspend fun getAll(walletId: String): List<InvoiceEntity> = invoiceDao.getAll(walletId)
}
