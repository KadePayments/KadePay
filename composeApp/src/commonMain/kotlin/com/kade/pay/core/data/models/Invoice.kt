package com.kade.pay.core.data.models

import com.kade.pay.core.wallet.Network
import kadepay.v1.services.invoice.InvoiceResponse

data class Invoice(
    val id: String? = null,
    val xPubKeyId: String,
    val chain: String,
    val network: Network,
    val currencyCode: String,
    val amount: Long,
    val address: String? = null,
    val createdAt: Long,
    val description: String? = null,
    val status: String? = null,
    val childKeyIndex: Int,
) {
    companion object {
        fun fromResponse(response: InvoiceResponse): Invoice =
            Invoice(
                response.id,
                response.x_pub_key_id,
                response.chain,
                Network.valueOf(response.network),
                response.currency_code,
                response.amount.toLong(),
                response.address,
                response.created_at,
                response.description,
                response.status,
                response.child_key_index,
            )
    }
}
