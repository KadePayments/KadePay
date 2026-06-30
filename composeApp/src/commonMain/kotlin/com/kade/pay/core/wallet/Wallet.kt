package com.kade.pay.core.wallet

import com.kade.pay.core.data.storage.SecureStorage
import com.kade.pay.core.secureRandom
import fr.acinq.bitcoin.Crypto
import fr.acinq.bitcoin.DeterministicWallet
import fr.acinq.bitcoin.KeyPath
import fr.acinq.bitcoin.MnemonicCode

interface Wallet {
    val masterPubKey: String
    val descriptor: String
    val lastUsedIndex: Int

    fun fingerprint(): String

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
        ): Wallet {
            val mnemonicString = mnemonics.joinToString(" ") { it }
            MnemonicCode.validate(mnemonicString)

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

            return WalletImpl(masterPublicKey, accountDescriptor, 0)
        }

        fun import(): Wallet {
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
