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
    private var onChainWallet: Wallet? by mutableStateOf(null)

    fun onLoadWallets() {
        viewModelScope.launch {
            val wallets = walletRepo.getAll()
            val onChainWallet = wallets.find { it.onChain }
            val onChainWalletAvailable = onChainWallet != null
            if (onChainWalletAvailable) {
                this@WalletViewModel.onChainWallet = onChainWallet
                state =
                    state.copy(
                        onChainWalletAvailable = true,
                    )
            }
        }
    }

    fun onNewWallet(onChain: Boolean) {
        viewModelScope.launch {
            if (onChain) {
                state =
                    state.copy(
                        mnemonics = BitcoinWallet.generateMnemonics(),
                    )
                return@launch
            }
            // Arkade OffChain Wallet
        }
    }

    fun onCreateWallet(
        passphrase: String,
        onChain: Boolean,
        secureStorage: SecureStorage,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            if (onChain) {
                if (state.mnemonics.isEmpty()) return@launch
                runCatching {
                    BitcoinWallet.new(passphrase, state.mnemonics, state.onChainNetwork, secureStorage)
                }.onSuccess {
                    onChainWallet = it
                    walletRepo.save(it)
                    state = state.copy(onChainWalletAvailable = true, mnemonics = emptyList())
                    onSuccess()
                }.onFailure {
                    state = state.copy(errorMessage = "Failed to create wallet")
                }
                return@launch
            }
            // Arkade OffChain Wallet
        }
    }

    fun onDeleteWallet() {
        TODO()
    }
}
