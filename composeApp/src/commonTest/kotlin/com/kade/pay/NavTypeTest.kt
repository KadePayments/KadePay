package com.kade.pay

import com.kade.pay.presentation.MapNavType
import kotlin.test.Test
import kotlin.test.assertEquals

class NavTypeTest {
    @Test
    fun serialize_and_parse_map_successfully() {
        val metadata = mapOf("key1" to "value1", "key2" to "value2")
        val serializedMetadata = "key1=value1&key2=value2"
        val serialized = MapNavType.serializeAsValue(metadata)
        assertEquals(serializedMetadata, serialized)
        val parsed = MapNavType.parseValue(serialized)
        assertEquals(metadata, parsed)
    }
}
