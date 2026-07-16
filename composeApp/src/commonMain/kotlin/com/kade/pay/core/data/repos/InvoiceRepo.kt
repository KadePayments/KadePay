package com.kade.pay.core.data.repos

import com.kade.pay.core.data.models.Invoice

interface InvoiceRepo {
    suspend fun save(invoice: Invoice)

    suspend fun save(invoices: List<Invoice>)

    suspend fun getAll(walletId: String): List<Invoice>
}
