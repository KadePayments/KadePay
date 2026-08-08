package com.kade.pay.presentation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write

object MapNavType : NavType<Map<String, String>>(false) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: Map<String, String>,
    ) {
        bundle.write {
            val stringList =
                value.map { entry ->
                    "${entry.key}:${entry.value}"
                }
            putStringList(key, stringList)
        }
    }

    override fun get(
        bundle: SavedState,
        key: String,
    ): Map<String, String> =
        bundle.read {
            getStringList(key).associate { item ->
                val (itemKey, value) = item.trim().split(":")
                itemKey to value
            }
        }

    override fun parseValue(value: String): Map<String, String> =
        value.removeSurrounding("{", "}").split(",").associate { item ->
            val (itemKey, value) = item.trim().split("=")
            itemKey to value.trim()
        }
}
