package com.kade.pay.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.kade.pay.core.data.db.Database
import com.kade.pay.core.data.repos.WalletRepoImpl
import com.kade.pay.core.data.storage.SecureStorage
import com.kade.pay.core.wallet.Wallet
import kotlinx.coroutines.launch

class WalletViewModel(
    dbBuilder: RoomDatabase.Builder<Database>,
) : ViewModel() {
    var state by mutableStateOf(WalletState())
        private set
    private val walletRepo = WalletRepoImpl(dbBuilder)
    private var wallet: Wallet? by mutableStateOf(null)

    fun onLoadWallets() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            runCatching { walletRepo.getAll().firstOrNull() }
                .onSuccess { wallet ->
                    val isWalletAvailable = wallet != null
                    if (isWalletAvailable) {
                        this@WalletViewModel.wallet = wallet
                        state = state.copy(isLoading = false, isWalletAvailable = true)
                        return@launch
                    }
                    this@WalletViewModel.wallet = null
                    clearMnemonics()
                    state = state.copy(isLoading = false, isWalletAvailable = false)
                }.onFailure {
                    wallet = null
                    state = state.copy(isLoading = false, isWalletAvailable = false, errorMessage = "Failed to load wallet")
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
                wallet = Wallet.new(passphrase, mnemonics, state.network, secureStorage)
                wallet?.let { walletRepo.save(it) }
            }.onSuccess {
                state = state.copy(passphrase = null, isWalletAvailable = true, mnemonics = emptyList())
                onSuccess()
            }.onFailure {
                state = state.copy(errorMessage = "Failed to create wallet")
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
}
