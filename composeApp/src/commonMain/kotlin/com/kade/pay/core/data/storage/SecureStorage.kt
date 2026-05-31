package com.kade.pay.core.data.storage

import androidx.compose.runtime.Composable

interface SecureStorage {
    suspend fun save(
        key: String,
        value: String,
    )

    suspend fun get(key: String): String?

    suspend fun delete(key: String)
}

@Composable
expect fun getSecureStorage(passphrase: String): SecureStorage
