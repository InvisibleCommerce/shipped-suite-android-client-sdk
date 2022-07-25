package com.invisiblecommerce.shippedsuite.exception

import com.invisiblecommerce.shippedsuite.model.ShippedError
import org.junit.Test
import kotlin.test.assertNotNull

class PermissionExceptionTest {

    @Test
    fun testEquals() {
        val permissionException = PermissionException(
            error = ShippedError()
        )
        assertNotNull(permissionException)
    }
}
