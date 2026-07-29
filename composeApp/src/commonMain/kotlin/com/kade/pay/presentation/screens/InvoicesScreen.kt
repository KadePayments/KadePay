package com.kade.pay.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kade.pay.core.data.models.BTC
import com.kade.pay.core.data.models.Invoice
import com.kade.pay.core.toBTCString
import com.kade.pay.presentation.screens.wallet.deriveToolTip
import com.kade.pay.presentation.theme.KadePayTheme
import com.kade.pay.presentation.views.PaymentStatusView
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.invoices
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoicesScreen(
    invoices: List<Invoice>,
    onSelectInvoice: (Invoice) -> Unit = {},
) {
    if (invoices.isNotEmpty()) {
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(start = 64.dp, end = 64.dp, top = 16.dp, bottom = 16.dp),
        ) {
            stickyHeader {
                Column {
                    Text(
                        stringResource(Res.string.invoices),
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )
                    Spacer(
                        Modifier
                            .height(16.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                    )
                }
            }
            items(
                invoices,
                key = { invoice -> invoice.id ?: invoice.hashCode() },
            ) { invoice ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            onSelectInvoice(invoice)
                        },
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val toolTipState = rememberTooltipState()
                        val tip = deriveToolTip(invoice.status)

                        TooltipBox(
                            positionProvider =
                                TooltipDefaults.rememberTooltipPositionProvider(
                                    TooltipAnchorPosition.Above,
                                ),
                            state = toolTipState,
                            tooltip = {
                                PlainTooltip {
                                    Text(tip)
                                }
                            },
                        ) {
                            PaymentStatusView(invoice.status)
                        }
                        Column(
                            Modifier.weight(1f),
                        ) {
                            if (invoice.address != null && invoice.id != null) {
                                Text(
                                    invoice.address,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "Id: ${invoice.id}",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                        Text(
                            "${BTC}${invoice.amount.toBTCString()}",
                            Modifier.padding(start = 12.dp, end = 12.dp).wrapContentWidth(),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider(thickness = 2.dp)
                }
            }
        }
    }
}

@Preview
@Composable
fun InvoicesScreenPreview() {
    KadePayTheme {
        InvoicesScreen(listOf())
    }
}
