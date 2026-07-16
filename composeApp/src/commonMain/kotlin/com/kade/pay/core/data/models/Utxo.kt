package com.kade.pay.core.data.models

data class Utxo(
    val address: String,
    val amount: Long,
    val invoiceId: String,
) {
    companion object {
        fun fromInvoice(invoice: Invoice): Utxo =
            Utxo(
                address = requireNotNull(invoice.address),
                amount = invoice.amount,
                invoiceId = requireNotNull(invoice.id),
            )
    }
}
