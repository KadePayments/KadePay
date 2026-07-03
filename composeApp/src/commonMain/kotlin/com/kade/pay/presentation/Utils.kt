package com.kade.pay.presentation

import androidx.compose.runtime.saveable.Saver
import fr.acinq.bitcoin.Crypto.sha256
import kotlin.collections.component1
import kotlin.collections.component2

val listSaver =
    Saver<MutableList<String>, String>(
        save = {
            it.joinToString()
        },
        restore = {
            if (it.isEmpty()) {
                mutableListOf<String>()
            } else {
                it.split(", ").toMutableList()
            }
        },
    )

val mapSaver =
    Saver<MutableMap<Int, Boolean>, String>(
        save = {
            it
                .map { (key, value) ->
                    "$key:$value"
                }.joinToString()
        },
        restore = {
            if (it.isEmpty()) {
                mutableMapOf<Int, Boolean>()
            } else {
                it
                    .split(", ")
                    .associate { entry ->
                        val (key, value) = entry.split(":", limit = 2)
                        key.trim().toInt() to value.trim().toBoolean()
                    }.toMutableMap()
            }
        },
    )

fun verifyPassphrase(
    passphrase: String,
    knownHash: String,
): Boolean {
    val passphraseHash = sha256(passphrase.encodeToByteArray()).toHexString()
    return passphraseHash == knownHash
}
