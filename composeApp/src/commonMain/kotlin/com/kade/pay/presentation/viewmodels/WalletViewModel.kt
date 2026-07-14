package com.kade.pay.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.kade.pay.core.data.db.Database
import com.kade.pay.core.data.repos.InvoiceRepo
import com.kade.pay.core.data.repos.InvoiceRepoImpl
import com.kade.pay.core.data.repos.WalletRepo
import com.kade.pay.core.data.repos.WalletRepoImpl
import com.kade.pay.core.data.storage.SecureStorage
import com.kade.pay.core.wallet.Network
import com.kade.pay.core.wallet.Wallet
import com.kade.pay.network.Config
import com.kade.pay.network.KadePayClient
import com.kade.pay.network.KadePayClientImpl
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class WalletViewModel(
    dbBuilder: RoomDatabase.Builder<Database>,
) : ViewModel() {
    var state by mutableStateOf(WalletState())
        private set
    private val walletRepo: WalletRepo = WalletRepoImpl(dbBuilder)
    private val invoiceRepo: InvoiceRepo = InvoiceRepoImpl(dbBuilder)
    private var wallet: Wallet? = null
    private var client: KadePayClient? = null

    fun onLoadWallets() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            runCatching { walletRepo.getAll().firstOrNull() }
                .onSuccess { wallet ->
                    val isWalletAvailable = wallet != null
                    if (isWalletAvailable) {
                        this@WalletViewModel.wallet = wallet
                        onLoadInvoices()
                        state = state.copy(isLoading = false, isWalletAvailable = true)
                        client = KadePayClientImpl(wallet.config)
                        state = state.copy(isLoading = false, isWalletAvailable = true, config = wallet.config)
                        return@launch
                    }
                    this@WalletViewModel.wallet = null
                    clearMnemonics()
                    state = state.copy(isLoading = false, isWalletAvailable = false)
                }.onFailure {
                    if (it is CancellationException) throw it
                    wallet = null
                    state = state.copy(isLoading = false, isWalletAvailable = false, errorMessage = "Failed to load wallet")
                }
        }
    }

    fun onLoadInvoices() {
        viewModelScope.launch {
            runCatching { invoiceRepo.getAll() }
                .onSuccess { invoices ->
                    state = state.copy(invoices = invoices)
                }.onFailure {
                    if (it is CancellationException) throw it
                    state = state.copy(errorMessage = "Failed to load invoices")
                }
        }
    }

    fun onNewWallet() {
        viewModelScope.launch {
            if (state.mnemonics.isNotEmpty()) {
                return@launch
            }
            state =
                state.copy(
                    mnemonics = Wallet.generateMnemonics(),
                )
            return@launch
        }
    }

    fun onCreateWallet(
        secureStorage: SecureStorage?,
        onSuccess: () -> Unit,
    ) {
        val passphrase = state.passphrase
        val mnemonics = state.mnemonics
        if (secureStorage == null || passphrase == null || mnemonics.isEmpty()) {
            state = state.copy(errorMessage = "Failed to create wallet")
            return
        }
        viewModelScope.launch {
            runCatching {
                state = state.copy(isLoading = true)
                wallet = Wallet.new(passphrase, mnemonics, secureStorage, state.config)
                wallet?.let { walletRepo.save(it) }

                client = KadePayClientImpl(wallet?.config!!)

                val masterPubKey = requireNotNull(wallet?.masterPubKey)
                val walletId = requireNotNull(client?.createWallet(masterPubKey))
                wallet?.updateWalletId(walletId)
                walletRepo.updateWalletId(masterPubKey, walletId)
            }.onSuccess {
                state = state.copy(isLoading = false, passphrase = null, isWalletAvailable = true, mnemonics = emptyList())
                onSuccess()
            }.onFailure {
                if (it is CancellationException) throw it
                state = state.copy(isLoading = false, errorMessage = "Failed to create wallet")
            }
            return@launch
        }
    }

    fun updatePassphrase(value: String) {
        state = state.copy(passphrase = value)
    }

    fun clearMnemonics() {
        state = state.copy(mnemonics = emptyList())
    }

    fun onDeleteWallet() {
        TODO()
    }

    fun onShowKeys() {
        if (wallet != null) {
            state =
                state.copy(
                    pubKey = wallet?.masterPubKey,
                    walletDescriptor = wallet?.descriptor,
                )
        }
    }

    fun onClearKeys() {
        state = state.copy(pubKey = null, walletDescriptor = null)
    }

    fun onUpdateConfig(
        url: String,
        network: Network,
    ) {
        state = state.copy(config = Config.from(url, network.name))
    }
}
