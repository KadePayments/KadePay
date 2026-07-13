package com.kade.pay.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kade.pay.core.wallet.Wallet
import com.kade.pay.core.wallet.WalletImpl
import com.kade.pay.network.Config

@Entity(
    tableName = "wallets",
)
data class WalletEntity(
    @PrimaryKey
    val masterPubKey: String,
    val descriptor: String,
    val lastUsedIndex: Int,
    val network: String,
    val serverUrl: String,
) {
    fun toWallet(): Wallet = WalletImpl(masterPubKey, descriptor, lastUsedIndex, Config.from(serverUrl, network))

    companion object {
        fun fromWallet(wallet: Wallet): WalletEntity =
            WalletEntity(
                wallet.masterPubKey,
                wallet.descriptor,
                wallet.lastUsedIndex,
                wallet.config.network.name,
                wallet.config.kadePayUrl,
            )
    }
}
