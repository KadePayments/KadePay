package com.kade.pay.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kade.pay.core.wallet.Wallet
import com.kade.pay.core.wallet.bitcoin.BitcoinWalletImpl

@Entity(
    tableName = "wallets",
)
data class WalletEntity(
    @PrimaryKey
    val masterPubKey: String,
    val descriptor: String,
    val mnemonic: String,
    val lastUsedIndex: Int,
    val onChain: Boolean,
) {
    fun toWallet(): Wallet =
        when (onChain) {
            true -> BitcoinWalletImpl(masterPubKey, descriptor, mnemonic, lastUsedIndex, onChain)
            else -> TODO()
        }

    companion object {
        fun fromWallet(wallet: Wallet): WalletEntity =
            WalletEntity(
                wallet.masterPubKey,
                wallet.descriptor,
                wallet.mnemonic,
                wallet.lastUsedIndex,
                wallet.onChain,
            )
    }
}
