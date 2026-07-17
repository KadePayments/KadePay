package com.kade.pay.core.data.models

import com.kade.pay.core.wallet.Network
import kadepay.v1.services.invoice.InvoiceResponse

data class Invoice(
    val id: String? = null,
    val xPubKeyId: String,
    val chain: Chain,
    val network: Network,
    val currencyCode: String,
    val amount: Long,
    val address: String? = null,
    val createdAt: Long,
    val description: String? = null,
    val status: PaymentStatus = PaymentStatus.PENDING,
    val childKeyIndex: Int,
) {
    companion object {
        fun fromResponse(response: InvoiceResponse): Invoice {
            val amount = response.amount.toLongOrNull()
            requireNotNull(amount) { "Invalid invoice amount" }
            return Invoice(
                id = response.id,
                xPubKeyId = response.x_pub_key_id,
                chain = Chain.fromString(response.chain),
                network = Network.fromString(response.network),
                currencyCode = response.currency_code,
                amount = amount,
                address = response.address,
                createdAt = response.created_at,
                description = response.description,
                status = PaymentStatus.fromString(response.status),
                childKeyIndex = response.child_key_index,
            )
        }
    }
}
