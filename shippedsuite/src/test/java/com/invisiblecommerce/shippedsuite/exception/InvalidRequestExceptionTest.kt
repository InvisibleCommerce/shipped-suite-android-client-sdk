package com.invisiblecommerce.shippedsuite.exception

import com.invisiblecommerce.shippedsuite.model.ShippedError
import org.junit.Test
import kotlin.test.assertEquals

class InvalidRequestExceptionTest {

    @Test
    fun testEquals() {
        val error = ShippedError(message = "message")
        val invalidRequestException = InvalidRequestException(
            param = "param",
            error
        )

        assertEquals("param", invalidRequestException.param)
        assertEquals("message", invalidRequestException.message)
    }
}
