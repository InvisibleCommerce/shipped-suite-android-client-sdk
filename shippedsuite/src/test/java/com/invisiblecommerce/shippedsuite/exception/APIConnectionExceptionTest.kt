package com.invisiblecommerce.shippedsuite.exception

import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class APIConnectionExceptionTest {

    @Test
    fun testEquals() {
        val apiException = APIConnectionException(
            message = "error",
            e = Exception()
        )

        assertNotNull(apiException)
        assertEquals("error", apiException.message)

        val exception = APIConnectionException.create(IOException(), "")
        assertNotNull(exception)
    }
}
