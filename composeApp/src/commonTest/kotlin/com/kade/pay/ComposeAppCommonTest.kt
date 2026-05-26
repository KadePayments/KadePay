package com.kade.pay

import com.kade.pay.core.wallet.Network
import com.kade.pay.core.wallet.bitcoin.BitcoinWallet
import com.kade.pay.core.wallet.bitcoin.BitcoinWallet.Companion.generateMnemonics
import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeAppCommonTest {
    @Test
    fun example() {
        assertEquals(3, 1 + 2)
    }

    @Test
    fun `create bitcoin wallet`() {
        val mnemonics = generateMnemonics()
        BitcoinWallet.new("passphrase", mnemonics, Network.MAINNET)
    }
}
