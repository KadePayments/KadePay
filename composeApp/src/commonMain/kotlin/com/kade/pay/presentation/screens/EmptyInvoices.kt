package com.kade.pay.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kade.pay.presentation.theme.KadePayTheme
import kadepay.composeapp.generated.resources.Res
import kadepay.composeapp.generated.resources.no_invoices
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmptyInvoices() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            stringResource(Res.string.no_invoices),
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview
@Composable
fun EmptyInvoicesPreview() {
    KadePayTheme {
        EmptyInvoices()
    }
}
