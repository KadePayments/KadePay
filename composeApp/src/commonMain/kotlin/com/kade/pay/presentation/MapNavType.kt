package com.kade.pay.presentation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlinx.serialization.json.Json

object MapNavType : NavType<Map<String, String>>(false) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: Map<String, String>,
    ) = bundle.write { putString(key, Json.encodeToString(value)) }

    override fun get(
        bundle: SavedState,
        key: String,
    ): Map<String, String> = bundle.read { Json.decodeFromString(getString(key)) }

    override fun parseValue(value: String): Map<String, String> = Json.decodeFromString(value)

    override fun serializeAsValue(value: Map<String, String>): String =
        value
            .map {
                "${it.key}=${it.value}"
            }.joinToString("&")
}
