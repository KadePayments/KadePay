package com.kade.pay.core.data.models

import com.kade.pay.presentation.screens.SelectedNavItem

enum class Chain {
    BITCOIN,
    ARKADE,
    ;

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
