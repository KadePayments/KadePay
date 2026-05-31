package com.kade.pay

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.kade.pay.core.data.storage.SecureStorage
import com.kade.pay.core.data.storage.SecureStorageImpl
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
actual abstract class Test {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    actual val secureStorage: SecureStorage = SecureStorageImpl(context)
}
