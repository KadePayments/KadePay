package com.kade.pay.core.data.repos

import com.kade.pay.core.data.models.Invoice

interface InvoiceRepo {
    suspend fun save(invoice: Invoice)

    suspend fun getAll(): List<Invoice>
}
