package com.kade.pay.core.data.models

import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.kade.pay.presentation.screens.SelectedNavItem
import kotlinx.serialization.Serializable

@Serializable
enum class Chain {
    BITCOIN,
    ARKADE,
    ;

    override fun toString(): String = super.toString().lowercase()

    object NavType : androidx.navigation.NavType<Chain>(false) {
        override fun put(
            bundle: SavedState,
            key: String,
            value: Chain,
        ) {
            bundle.write {
                putString(key, value.toString())
            }
        }

        override fun get(
            bundle: SavedState,
            key: String,
        ): Chain = bundle.read { fromString(getString(key)) }

        override fun parseValue(value: String): Chain = fromString(value)
    }

    companion object {
        fun fromString(chain: String): Chain =
            when (chain.lowercase()) {
                "bitcoin" -> BITCOIN
                "arkade" -> ARKADE
                else -> throw IllegalArgumentException("Invalid chain: $chain")
            }

        fun fromNavItem(navItem: SelectedNavItem): Chain =
            when (navItem) {
                is SelectedNavItem.Bitcoin -> BITCOIN
                is SelectedNavItem.Arkade -> ARKADE
                else -> throw IllegalArgumentException("Invalid nav item: $navItem")
            }
    }
}
