package com.kade.pay.core.wallet.bitcoin

import com.kade.pay.core.wallet.Network
import com.kade.pay.core.wallet.Wallet
import fr.acinq.bitcoin.Crypto
import fr.acinq.bitcoin.DeterministicWallet
import fr.acinq.bitcoin.KeyPath
import fr.acinq.bitcoin.MnemonicCode
import kotlin.random.Random

interface BitcoinWallet : Wallet {
    companion object {
        fun generateMnemonics(): List<String> {
            val entropy = ByteArray(32)
            Random.nextBytes(entropy)
            return MnemonicCode.toMnemonics(entropy)
        }

        fun new(
            passphrase: String,
            mnemonics: List<String>,
            network: Network,
        ): BitcoinWallet {
            val seed = MnemonicCode.toSeed(mnemonics, passphrase)
            val masterKey = DeterministicWallet.generate(seed)
            val keyFingerprint = masterKey.extendedPublicKey.keyFingerprint()

            val coinType =
                when (network) {
                    Network.MAINNET -> 0
                    else -> 1
                }

            val keyPath = KeyPath("m/86'/$coinType'/0'")

            val accountPrivateKey = masterKey.derivePrivateKey(keyPath)
            val accountPubKey =
                when (network) {
                    Network.MAINNET -> accountPrivateKey.extendedPublicKey.encode(false)
                    else -> accountPrivateKey.extendedPublicKey.encode(true)
                }

            val accountDescriptor = "tr([$keyFingerprint/86'/$coinType'/0']$accountPubKey/0/*)"

            val mnemonicString = mnemonics.joinToString(" ") { it }
            MnemonicCode.validate(mnemonicString)
            return BitcoinWalletImpl(accountDescriptor, mnemonicString, 0)
        }

        fun import(): BitcoinWallet {
            TODO("Not implemented yet")
        }

        fun DeterministicWallet.ExtendedPublicKey.keyFingerprint(): String =
            Crypto
                .hash160(publickeybytes)
                .take(4)
                .toByteArray()
                .toHexString()
    }
}
