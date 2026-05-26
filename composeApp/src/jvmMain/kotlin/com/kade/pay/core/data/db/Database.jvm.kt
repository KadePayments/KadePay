package com.kade.pay.core.data.db

import androidx.compose.runtime.Composable
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

@Composable
actual fun getDatabaseBuilder(): RoomDatabase.Builder<Database> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "kadepay.db")
    return Room.databaseBuilder(
        name = dbFile.absolutePath,
    ) { DatabaseConstructor.initialize() }
}
