package com.invisiblecommerce.shippedsuite.model.parser

import org.json.JSONObject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ShippedErrorParserTest {

    @Test
    fun parserTest() {
        val parser = ShippedErrorParser()
        assertNotNull(parser.dateFormat)

        val jsonObject = JSONObject().put("error", "Auth error")
        val ShippedError = parser.parse(jsonObject)
        assertEquals(ShippedError.message, "Auth error")
    }
}
