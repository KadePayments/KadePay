package com.kade.pay.core.data.storage

import com.kade.pay.core.data.db.entities.InvoiceEntity

interface InvoiceStorage {
    suspend fun save(invoice: InvoiceEntity)

    suspend fun getAll(): List<InvoiceEntity>
}
