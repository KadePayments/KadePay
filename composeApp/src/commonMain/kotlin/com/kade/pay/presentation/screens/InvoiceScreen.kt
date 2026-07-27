package com.kade.pay.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kade.pay.core.data.models.BTC
import com.kade.pay.core.data.models.Chain
import com.kade.pay.core.data.models.Invoice
import com.kade.pay.core.data.models.PaymentStatus
import com.kade.pay.core.toBTCString
import com.kade.pay.core.toDateTimeString
import com.kade.pay.core.wallet.Network
import com.kade.pay.presentation.theme.KadePayTheme
import com.kade.pay.presentation.views.PaymentStatusView
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.arrow_back
import kadepay.composeapp.generated.resources.back
import kadepay.composeapp.generated.resources.btc_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun InvoiceScreen(
    invoice: Invoice,
    onBack: () -> Unit = {},
) {
    Box(Modifier.fillMaxSize()) {
        IconButton(
            onClick = onBack,
            Modifier.padding(16.dp).align(Alignment.TopStart),
        ) {
            Icon(
                painterResource(Res.drawable.arrow_back),
                stringResource(Res.string.back),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }

        Card(
            Modifier.align(Alignment.Center).padding(start = 16.dp, end = 16.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painterResource(Res.drawable.btc_logo),
                        stringResource(Res.string.back),
                    )
                    Column(Modifier.padding(start = 12.dp)) {
                        Text("$BTC${invoice.amount.toBTCString()}")
                        Text(invoice.createdAt.toDateTimeString())
                    }

                    Spacer(Modifier.fillMaxWidth().weight(1f))
                    PaymentStatusView(invoice.status)
                }

                Column(Modifier.padding(start = 16.dp)) {
                    Text("Invoice Id: ${invoice.id}")
                    Text("TxId: Waiting for confirmation (1/6)")

                    if (invoice.description != null) {
                        Text(invoice.description)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun InvoiceScreenPreview() {
    KadePayTheme {
        InvoiceScreen(
            Invoice(
                id = null,
                xPubKeyId = "",
                chain = Chain.BITCOIN,
                network = Network.TESTNET,
                currencyCode = "SATS",
                amount = 10000,
                address = null,
                createdAt = 70000000,
                description = null,
                status = PaymentStatus.PENDING,
                childKeyIndex = 0,
            ),
        )
    }
}
