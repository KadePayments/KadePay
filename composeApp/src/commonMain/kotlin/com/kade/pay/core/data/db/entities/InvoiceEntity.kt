package com.kade.pay.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kade.pay.core.data.db.MapStringConverter
import com.kade.pay.core.data.models.Chain
import com.kade.pay.core.data.models.Invoice
import com.kade.pay.core.data.models.PaymentStatus
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
    val description: String?,
    val status: String,
    val childKeyIndex: Int,
    @TypeConverters(MapStringConverter::class)
    val metadata: Map<String, String>,
) {
    fun toInvoice(): Invoice =
        Invoice(
            id,
            xPubKeyId,
            Chain.fromString(chain),
            network,
            currencyCode,
            amount,
            address,
            createdAt,
            description,
            PaymentStatus.fromString(status),
            childKeyIndex,
            metadata,
        )

    companion object {
        fun fromInvoice(invoice: Invoice): InvoiceEntity {
            val id = requireNotNull(invoice.id)
            val address = requireNotNull(invoice.address)
            val status = requireNotNull(invoice.status)
            return InvoiceEntity(
                id,
                invoice.xPubKeyId,
                invoice.chain.toString(),
                invoice.network,
                invoice.currencyCode,
                invoice.amount,
                address,
                invoice.createdAt,
                invoice.description,
                status.name.lowercase(),
                invoice.childKeyIndex,
                invoice.metadata,
            )
        }
    }
}
