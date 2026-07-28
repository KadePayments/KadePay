package com.kade.pay.presentation.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kade.pay.core.data.models.PaymentStatus
import com.kade.pay.presentation.screens.wallet.deriveToolTip
import com.kade.pay.presentation.theme.KadePayTheme
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.done
import kadepay.composeapp.generated.resources.info
import kadepay.composeapp.generated.resources.pending
import kadepay.composeapp.generated.resources.schedule
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentStatusView(status: PaymentStatus) {
    val toolTipState = rememberTooltipState()
    val tip = deriveToolTip(status)

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
        when (status) {
            PaymentStatus.PENDING -> {
                StatusView(status, Res.drawable.pending, MaterialTheme.colorScheme.errorContainer)
            }
            PaymentStatus.PAID -> {
                StatusView(status, Res.drawable.schedule, MaterialTheme.colorScheme.primary)
            }
            PaymentStatus.CONFIRMED -> {
                StatusView(status, Res.drawable.done, MaterialTheme.colorScheme.primary)
            }
            PaymentStatus.EXPIRED -> {
                StatusView(status, Res.drawable.schedule, MaterialTheme.colorScheme.outlineVariant)
            }
            PaymentStatus.CANCELLED -> {
                StatusView(status, Res.drawable.info, MaterialTheme.colorScheme.error)
            }

            PaymentStatus.UNKNOWN -> {
                StatusView(status, Res.drawable.info, MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}

@Composable
private fun StatusView(
    status: PaymentStatus,
    icon: DrawableResource,
    color: Color,
) {
    Row {
        Text(status.toString(), Modifier.padding(end = 8.dp))
        Icon(
            painterResource(icon),
            status.toString(),
            tint = color,
        )
    }
}

@Preview
@Composable
fun PaymentStatusViewPreview() {
    KadePayTheme {
        PaymentStatusView(PaymentStatus.PENDING)
    }
}
