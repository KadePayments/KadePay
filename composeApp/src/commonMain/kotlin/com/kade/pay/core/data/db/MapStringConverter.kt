package com.kade.pay.core.data.db

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class MapStringConverter {
    @TypeConverter
    fun from(map: Map<String, String>): String = Json.encodeToString(map)

    @TypeConverter
    fun to(string: String): Map<String, String> = Json.decodeFromString(string)
}
