package com.kade.pay.core.data.models

data class Utxo(
    val address: String,
    val amount: Long,
    val invoiceId: String,
    val status: PaymentStatus,
    val chain: Chain,
) {
    companion object {
        fun fromInvoice(invoice: Invoice): Utxo =
            Utxo(
                address = requireNotNull(invoice.address),
                amount = invoice.amount,
                invoiceId = requireNotNull(invoice.id),
                status = invoice.status,
                chain = invoice.chain,
            )
    }
}

fun List<Utxo>.filterByChain(chain: Chain) = this.filter { it.chain == chain }

fun List<Utxo>.sumOfConfirmedUtxos(): Long = this.filter { it.status == PaymentStatus.CONFIRMED }.sumOf { it.amount }
