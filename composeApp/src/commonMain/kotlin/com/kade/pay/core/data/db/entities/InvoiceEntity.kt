package com.kade.pay.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kade.pay.core.data.models.Invoice
import com.kade.pay.core.wallet.Network

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey
    val id: String,
    val xPubKeyId: String,
    val chain: String,
    val network: Network,
    val currencyCode: String,
    val amount: Long,
    val address: String,
    val createdAt: Long,
    val description: String,
    val status: String,
    val childKeyIndex: Int,
) {
    fun toInvoice(): Invoice =
        Invoice(
            id,
            xPubKeyId,
            chain,
            network,
            currencyCode,
            amount,
            address,
            createdAt,
            description,
            status,
            childKeyIndex,
        )

    companion object {
        fun fromInvoice(invoice: Invoice): InvoiceEntity =
            InvoiceEntity(
                requireNotNull(invoice.id),
                invoice.xPubKeyId,
                invoice.chain,
                invoice.network,
                invoice.currencyCode,
                invoice.amount,
                requireNotNull(invoice.address),
                invoice.createdAt,
                requireNotNull(invoice.description),
                requireNotNull(invoice.status),
                invoice.childKeyIndex,
            )
    }
}
