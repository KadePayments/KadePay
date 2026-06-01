package com.kade.pay.core.data.db

import androidx.compose.runtime.Composable
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.kade.pay.core.data.db.dao.WalletDao
import com.kade.pay.core.data.db.entities.WalletEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [WalletEntity::class],
    version = 1,
    exportSchema = true,
)
@ConstructedBy(DatabaseConstructor::class)
abstract class Database : RoomDatabase() {
    abstract fun walletDao(): WalletDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object DatabaseConstructor : RoomDatabaseConstructor<com.kade.pay.core.data.db.Database> {
    override fun initialize(): com.kade.pay.core.data.db.Database
}

fun RoomDatabase.Builder<com.kade.pay.core.data.db.Database>.getDatabase(): com.kade.pay.core.data.db.Database =
    setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()

@Composable
expect fun getDatabaseBuilder(): RoomDatabase.Builder<com.kade.pay.core.data.db.Database>
