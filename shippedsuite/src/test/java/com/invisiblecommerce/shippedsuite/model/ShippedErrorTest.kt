package com.invisiblecommerce.shippedsuite.model

import org.junit.Test
import kotlin.test.assertEquals

class ShippedErrorTest {

    @Test
    fun testParams() {
        val error = ShippedErrorFixtures.Error
        assertEquals("Auth error", error.message)
    }
}
