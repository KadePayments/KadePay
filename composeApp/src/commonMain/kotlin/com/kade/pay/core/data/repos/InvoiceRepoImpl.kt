package com.kade.pay.core.data.repos

import androidx.room.RoomDatabase
import com.kade.pay.core.data.db.Database
import com.kade.pay.core.data.db.entities.InvoiceEntity
import com.kade.pay.core.data.models.Invoice
import com.kade.pay.core.data.storage.InvoiceStorage
import com.kade.pay.core.data.storage.InvoiceStorageImpl

class InvoiceRepoImpl(
    databaseBuilder: RoomDatabase.Builder<Database>,
) : InvoiceRepo {
    private val invoiceStorage: InvoiceStorage = InvoiceStorageImpl(databaseBuilder)

    override suspend fun save(invoice: Invoice) {
        val entity = InvoiceEntity.fromInvoice(invoice)
        invoiceStorage.save(entity)
    }

    override suspend fun save(invoices: List<Invoice>) {
        val entities = invoices.map { invoice -> InvoiceEntity.fromInvoice(invoice) }
        invoiceStorage.save(entities)
    }

    override suspend fun getAll(walletId: String): List<Invoice> {
        val entities = invoiceStorage.getAll(walletId)
        return entities.map { it.toInvoice() }
    }
}
