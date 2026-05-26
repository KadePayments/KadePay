package com.kade.pay.core.data.db

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import androidx.room.RoomDatabase

@Composable
actual fun getDatabaseBuilder(): RoomDatabase.Builder<Database> {
    val appContext = LocalContext.current.applicationContext
    val dbFile = appContext.getDatabasePath("kadepay.db")
    return Room.databaseBuilder(
        context = appContext,
        name = dbFile.absolutePath,
    ) {
        DatabaseConstructor.initialize()
    }
}
