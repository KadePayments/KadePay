package com.kade.pay.core.data.db

import androidx.compose.runtime.Composable
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

@Composable
actual fun getDatabaseBuilder(): RoomDatabase.Builder<Database> {
    val appDir =
        File(System.getProperty("user.home"), ".kadepay").also {
            if (!it.exists() && !it.mkdirs()) {
                throw IllegalArgumentException("Failed to create app directory: ${it.absolutePath}")
            }
        }
    val dbFile = File(appDir, "kadepay.db")
    return Room.databaseBuilder(
        name = dbFile.absolutePath,
    ) { DatabaseConstructor.initialize() }
}
