package com.invisiblecommerce.shippedsuite.exception

import org.junit.Test
import kotlin.test.assertEquals

class InvalidParamsExceptionTest {

    @Test
    fun testEquals() {
        val invalidParamsException = InvalidParamsException(
            message = "message"
        )

        assertEquals("message", invalidParamsException.message)
    }
}