package com.invisiblecommerce.shippedsuite

import org.junit.Assert.assertNotEquals
import org.junit.Test

class ModeTest {
    @Test
    fun isBaseUrlNull() {
        assertNotEquals(Mode.DEVELOPMENT.baseUrl(), null)
        assertNotEquals(Mode.PRODUCTION.baseUrl(), null)
    }
}
