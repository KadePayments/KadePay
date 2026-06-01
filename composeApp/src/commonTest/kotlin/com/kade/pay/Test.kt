package com.kade.pay

import com.kade.pay.core.data.storage.SecureStorage

expect abstract class Test() {
    val secureStorage: SecureStorage
}
