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
import com.kade.pay.core.wallet.bitcoin.BitcoinWallet
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
            val wallets = walletRepo.getAll()
            val wallet = wallets.firstOrNull()
            val isWalletAvailable = wallet != null
            if (isWalletAvailable) {
                this@WalletViewModel.wallet = wallet
                state =
                    state.copy(
                        isWalletAvailable = true,
                    )
                return@launch
            }
            clearMnemonics()
        }
    }

    fun onNewWallet() {
        viewModelScope.launch {
            if (state.mnemonics.isNotEmpty()) {
                return@launch
            }
            state =
                state.copy(
                    mnemonics = BitcoinWallet.generateMnemonics(),
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
                wallet = BitcoinWallet.new(passphrase, mnemonics, state.network, secureStorage)
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
