package com.kade.pay.presentation.screens.invoices

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kade.pay.core.data.models.Chain
import com.kade.pay.core.data.models.Invoice
import com.kade.pay.core.data.models.PaymentStatus
import com.kade.pay.core.wallet.Network
import com.kade.pay.presentation.MapNavType
import com.kade.pay.presentation.screens.EmptyInvoices
import com.kade.pay.presentation.screens.InvoiceScreen
import com.kade.pay.presentation.screens.InvoicesScreen
import com.kade.pay.presentation.viewmodels.WalletState
import kotlin.reflect.typeOf

@Composable
fun MainInvoicesScreen(state: WalletState) {
    val navController = rememberNavController()

    NavHost(navController, INVOICES) {
        composable(INVOICES) {
            if (state.invoices.isEmpty()) {
                EmptyInvoices()
                return@composable
            }
            InvoicesScreen(state.invoices) { invoice ->
                navController.navigate(invoice)
            }
        }
        composable<Invoice>(
            typeMap =
                mapOf(
                    typeOf<Chain>() to Chain.NavType,
                    typeOf<Network>() to Network.NavType,
                    typeOf<PaymentStatus>() to PaymentStatus.NavType,
                    typeOf<Map<String, String>>() to MapNavType,
                ),
        ) {
            val invoice = it.toRoute<Invoice>()
            InvoiceScreen(invoice) {
                navController.popBackStack()
            }
        }
    }
}
