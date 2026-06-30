package com.kade.pay

import com.kade.pay.core.wallet.Network
import com.kade.pay.core.wallet.Wallet
import com.kade.pay.core.wallet.Wallet.Companion.generateMnemonics
import com.kade.pay.core.wallet.Wallet.Companion.keyFingerprint
import fr.acinq.bitcoin.DeterministicWallet
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BitcoinWalletTest : com.kade.pay.Test() {
    @Test
    fun `should create bitcoin wallet successfully`() {
        runTest {
            val passphrase = "passphrase"
            val mnemonics = generateMnemonics()
            val wallet =
                Wallet.new(passphrase, mnemonics, Network.TESTNET, secureStorage)
            val masterPrivateKey = secureStorage.get(wallet.fingerprint())

            assertNotNull(masterPrivateKey)

            val masterKey =
                DeterministicWallet.ExtendedPrivateKey.decode(masterPrivateKey).second

            assertEquals(wallet.fingerprint(), masterKey.extendedPublicKey.keyFingerprint())
            assertEquals(wallet.masterPubKey, masterKey.extendedPublicKey.encode(true))

            secureStorage.delete(wallet.fingerprint())
            assertEquals(null, secureStorage.get(wallet.fingerprint()))
        }
    }
}
