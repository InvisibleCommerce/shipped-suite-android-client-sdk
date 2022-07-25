package com.invisiblecommerce.shippedsuite.model

import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ShippedRequestTest {

    private val defaultOrderValue = BigDecimal.valueOf(129.99)

    private val request = ShippedRequest.Builder().setOrderValue(defaultOrderValue).build()
    private val emptyRequest = ShippedRequest.Builder().build()

    @Test
    fun testParams() {
        assertNotNull(request.toParamMap())
        assertEquals(ShippedRequest.Builder().setOrderValue(defaultOrderValue).build().toParamMap(), request.toParamMap())
    }

    @Test
    fun testEmptyParams() {
        assertNotNull(emptyRequest.toParamMap())
        assertEquals(ShippedRequest.Builder().build().toParamMap(), emptyRequest.toParamMap())
    }

    @Test
    fun testParamsMap() {
        val paramMap = request.toParamMap()
        assertEquals(
            mapOf(
                "order_value" to defaultOrderValue
            ),
            paramMap
        )
    }
}
