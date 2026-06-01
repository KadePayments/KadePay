package com.kade.pay.core.data.storage

import android.content.Context
import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.flow.first
import kotlin.text.Charsets.UTF_8

class SecureStorageImpl(
    private val context: Context,
) : SecureStorage {
    private val Context.dataStore by preferencesDataStore("kade_pay_secrets")
    private val crypto = CryptoManager(context)

    override suspend fun save(
        key: String,
        value: String,
    ) {
        val encryptedValue = crypto.encrypt(value)
        val stringPreferenceKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[stringPreferenceKey] = encryptedValue
        }
    }

    override suspend fun get(key: String): String? {
        val preferences = context.dataStore.data.first()
        val stringPreferenceKey = stringPreferencesKey(key)
        val encryptedValue = preferences[stringPreferenceKey]
        return if (encryptedValue != null) {
            crypto.decrypt(encryptedValue)
        } else {
            null
        }
    }

    override suspend fun delete(key: String) {
        val stringPreferenceKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferenceKey)
        }
    }
}

class CryptoManager(
    context: Context,
) {
    private val aead: Aead

    init {
        AeadConfig.register()
        val keysetURI = "android-keystore://master_key"

        val keysetManager =
            AndroidKeysetManager
                .Builder()
                .withSharedPref(context, "tink_keyset", "tink_master_key")
                .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
                .withMasterKeyUri(keysetURI)
                .build()

        aead = keysetManager.keysetHandle.getPrimitive(Aead::class.java)
    }

    fun encrypt(string: String): String {
        val bytes = string.encodeToByteArray()
        val encryptedBytes = aead.encrypt(bytes, null)
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(base64EncodedString: String): String {
        val bytes = Base64.decode(base64EncodedString, Base64.DEFAULT)
        val decryptedBytes = aead.decrypt(bytes, null)
        return String(decryptedBytes, UTF_8)
    }
}

@Composable
actual fun getSecureStorage(passphrase: String): SecureStorage {
    val appContext = LocalContext.current.applicationContext
    return SecureStorageImpl(appContext)
}
