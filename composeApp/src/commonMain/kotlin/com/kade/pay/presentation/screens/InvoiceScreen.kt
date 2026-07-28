package com.kade.pay.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kade.pay.core.data.models.BTC
import com.kade.pay.core.data.models.Chain
import com.kade.pay.core.data.models.Invoice
import com.kade.pay.core.data.models.PaymentStatus
import com.kade.pay.core.generateQRCode
import com.kade.pay.core.getBytes
import com.kade.pay.core.getImageBitmap
import com.kade.pay.core.toBTCString
import com.kade.pay.core.toDateTimeString
import com.kade.pay.core.wallet.Network
import com.kade.pay.presentation.theme.KadePayTheme
import com.kade.pay.presentation.views.PaymentStatusView
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.arkade
import kadepay.composeapp.generated.resources.arrow_back
import kadepay.composeapp.generated.resources.back
import kadepay.composeapp.generated.resources.kade
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

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
            Modifier.wrapContentWidth().align(Alignment.Center).padding(start = 16.dp, end = 16.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painterResource(Res.drawable.arkade),
                        stringResource(Res.string.back),
                    )
                    Column(Modifier.padding(start = 12.dp)) {
                        Text(
                            "$BTC${invoice.amount.toBTCString()}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            invoice.createdAt.toDateTimeString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                        )
                    }

                    Spacer(Modifier.fillMaxWidth().weight(1f))
                    PaymentStatusView(invoice.status)
                }

                val logo = vectorResource(Res.drawable.kade).getImageBitmap()
                var qrCodeImage by remember { mutableStateOf<ImageBitmap?>(null) }
                val qrCodeColor = MaterialTheme.colorScheme.onPrimaryContainer.toArgb()

                LaunchedEffect(invoice.id) {
                    if (invoice.address != null) {
                        qrCodeImage = generateQRCode(invoice.address, qrCodeColor, logo.getBytes(), logo.width)
                    }
                }

                if (qrCodeImage != null) {
                    Image(
                        qrCodeImage!!,
                        "QR Code",
                        Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(16.dp)
                            .fillMaxWidth(),
                    )
                }

                Column(Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp).align(Alignment.Start)) {
                    Text("Invoice Id: ${invoice.id}")
                    Spacer(Modifier.height(12.dp))
                    Text("TxId: Waiting for confirmation (1/6)")

                    if (invoice.description != null) {
                        Spacer(Modifier.height(12.dp))
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
