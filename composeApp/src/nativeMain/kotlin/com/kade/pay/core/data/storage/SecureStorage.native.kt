package com.kade.pay.core.data.storage

import androidx.compose.runtime.Composable

class SecureStorageImpl : SecureStorage {
    override suspend fun save(
        key: String,
        value: String,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun get(key: String): String? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(key: String) {
        TODO("Not yet implemented")
    }
}

@Composable
actual fun getSecureStorage(passphrase: String): SecureStorage {
    TODO("Not yet implemented")
}
