package com.kade.pay.presentation.screens.navigation

import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.kade.pay.core.data.models.Chain
import com.kade.pay.core.data.models.Invoice
import com.kade.pay.core.data.models.PaymentStatus
import com.kade.pay.core.wallet.Network

private const val INVOICE_ID = "invoiceId"
private const val WALLET_ID = "walletId"
private const val ADDRESS = "address"
private const val AMOUNT = "amount"
private const val STATUS = "status"
private const val CURRENCY_CODE = "currencyCode"
private const val DESCRIPTION = "description"
private const val CHAIN = "chain"
private const val CHILD_KEY_INDEX = "childKeyIndex"
private const val NETWORK = "network"
private const val CREATED_AT = "createdAt"

fun SavedState.saveInvoice(invoice: Invoice) =
    write {
        putString(INVOICE_ID, invoice.id!!)
        putString(WALLET_ID, invoice.xPubKeyId)
        putString(ADDRESS, invoice.address!!)
        putLong(AMOUNT, invoice.amount)
        putString(STATUS, invoice.status.toString())
        putString(CURRENCY_CODE, invoice.currencyCode)
        putString(DESCRIPTION, invoice.description!!)
        putString(CHAIN, invoice.chain.toString())
        putInt(CHILD_KEY_INDEX, invoice.childKeyIndex)
        putString(NETWORK, invoice.network.toString())
        putLong(CREATED_AT, invoice.createdAt)
    }

fun SavedState.getInvoice(): Invoice =
    read {
        val id = getString(INVOICE_ID)
        val walletId = getString(WALLET_ID)
        val chain = Chain.fromString(getString(CHAIN))
        val network = Network.fromString(getString(NETWORK))
        val currencyCode = getString(CURRENCY_CODE)
        val amount = getLong(AMOUNT)
        val address = getString(ADDRESS)
        val createdAt = getLong(CREATED_AT)
        val description = getString(DESCRIPTION)
        val status = PaymentStatus.fromString(getString(STATUS))
        val childKeyIndex = getInt(CHILD_KEY_INDEX)
        Invoice(
            id,
            walletId,
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
    }
