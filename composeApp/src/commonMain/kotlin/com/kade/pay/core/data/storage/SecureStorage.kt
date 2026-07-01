package com.kade.pay.core.data.storage

import androidx.compose.runtime.Composable
import fr.acinq.bitcoin.Crypto.sha256

interface SecureStorage {
    suspend fun save(
        key: String,
        value: String,
    )

    suspend fun get(key: String): String?

    suspend fun delete(key: String)

    fun getSha256Key(key: String): String = sha256(key.encodeToByteArray()).toHexString()
}

@Composable
expect fun getSecureStorage(passphrase: String): SecureStorage
