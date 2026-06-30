package com.kade.pay.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kade.pay.core.wallet.Wallet
import com.kade.pay.core.wallet.WalletImpl

@Entity(
    tableName = "wallets",
)
data class WalletEntity(
    @PrimaryKey
    val masterPubKey: String,
    val descriptor: String,
    val lastUsedIndex: Int,
) {
    fun toWallet(): Wallet = WalletImpl(masterPubKey, descriptor, lastUsedIndex)

    companion object {
        fun fromWallet(wallet: Wallet): WalletEntity =
            WalletEntity(
                wallet.masterPubKey,
                wallet.descriptor,
                wallet.lastUsedIndex,
            )
    }
}
