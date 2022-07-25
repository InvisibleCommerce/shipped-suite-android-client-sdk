package com.invisiblecommerce.shippedsuite.exception

import com.invisiblecommerce.shippedsuite.model.ShippedError
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AuthenticationExceptionTest {

    @Test
    fun testEquals() {
        val authenticationException = AuthenticationException(
            error = ShippedError(
                message = "message"
            )
        )

        assertNotNull(authenticationException)
        assertEquals("message", authenticationException.message)
    }
}
