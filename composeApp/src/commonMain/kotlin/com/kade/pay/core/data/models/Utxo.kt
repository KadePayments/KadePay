package com.kade.pay.core.data.models

data class Utxo(
    val address: String,
    val amount: Float,
    val invoiceId: String,
)
