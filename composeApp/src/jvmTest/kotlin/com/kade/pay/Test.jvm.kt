package com.kade.pay

import com.kade.pay.core.data.storage.SecureStorage
import com.kade.pay.core.data.storage.SecureStorageImpl

actual abstract class Test {
    actual val secureStorage: SecureStorage = SecureStorageImpl("passphrase")
}
