package com.kade.pay.network

import com.kade.pay.core.data.models.Invoice

interface KadePayClient {
    suspend fun createWallet(masterPubKey: String): String

    suspend fun createInvoice(request: Invoice): Invoice

    suspend fun getInvoices(walletId: String): List<Invoice>
}
