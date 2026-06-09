package com.kade.pay.presentation

import androidx.compose.runtime.saveable.Saver
import kotlin.collections.component1
import kotlin.collections.component2

val listSaver =
    Saver<MutableList<String>, String>(
        save = {
            it.joinToString()
        },
        restore = {
            it.split(",").toMutableList()
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
            it
                .split(",")
                .associate { entry ->
                    val (key, value) = entry.split(":")
                    key.toInt() to value.toBoolean()
                }.toMutableMap()
        },
    )
