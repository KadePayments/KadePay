package com.kade.pay.core.wallet.bitcoin

import com.kade.pay.core.data.storage.SecureStorage
import com.kade.pay.core.secureRandom
import com.kade.pay.core.wallet.Network
import com.kade.pay.core.wallet.Wallet
import fr.acinq.bitcoin.Crypto
import fr.acinq.bitcoin.DeterministicWallet
import fr.acinq.bitcoin.KeyPath
import fr.acinq.bitcoin.MnemonicCode

interface BitcoinWallet : Wallet {
    companion object {
        fun generateMnemonics(): List<String> {
            val entropy = secureRandom()
            return MnemonicCode.toMnemonics(entropy)
        }

        suspend fun new(
            passphrase: String,
            mnemonics: List<String>,
            network: Network,
            secureStorage: SecureStorage,
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
            val masterPublicKey =
                when (network) {
                    Network.MAINNET -> masterKey.extendedPublicKey.encode(false)
                    else -> masterKey.extendedPublicKey.encode(true)
                }
            val masterKeyPrivateKey =
                when (network) {
                    Network.MAINNET -> masterKey.encode(false)
                    else -> masterKey.encode(true)
                }

            secureStorage.save(keyFingerprint, masterKeyPrivateKey)

            val accountDescriptor = "tr([$keyFingerprint/86'/$coinType'/0']$accountPubKey/0/*)"

            val mnemonicString = mnemonics.joinToString(" ") { it }
            MnemonicCode.validate(mnemonicString)
            return BitcoinWalletImpl(masterPublicKey, accountDescriptor, mnemonicString, 0)
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
