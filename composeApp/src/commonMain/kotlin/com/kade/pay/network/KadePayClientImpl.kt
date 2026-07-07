package com.kade.pay.network

import com.kade.pay.core.data.models.Invoice
import kadepay.v1.services.invoice.GetInvoicesRequest
import kadepay.v1.services.invoice.GrpcInvoiceServiceClient
import kadepay.v1.services.invoice.NewInvoiceRequest
import kadepay.v1.services.wallet.GrpcWalletServiceClient
import kadepay.v1.services.wallet.NewWalletRequest

class KadePayClientImpl(
    config: Config,
) : KadePayClient {
    private val grpcClient = grpcClient(config)
    private val walletClient = GrpcWalletServiceClient(grpcClient)
    private val invoicesClient = GrpcInvoiceServiceClient(grpcClient)

    override suspend fun createWallet(masterPubKey: String): String {
        val request = NewWalletRequest(masterPubKey)
        val response = walletClient.CreateWallet().execute(request)
        return response.x_pub_key_id
    }

    override suspend fun createInvoice(request: Invoice): Invoice {
        val request =
            NewInvoiceRequest(
                request.xPubKeyId,
                request.chain,
                request.network.name,
                request.currencyCode,
                request.amount.toString(),
                request.description ?: "",
            )
        val response = invoicesClient.CreateInvoice().execute(request)
        return Invoice.fromResponse(response)
    }

    override suspend fun getInvoices(walletId: String): List<Invoice> {
        val request = GetInvoicesRequest(walletId)
        val response = invoicesClient.GetInvoices().execute(request)
        return response.invoices.map { Invoice.fromResponse(it) }
    }
}
