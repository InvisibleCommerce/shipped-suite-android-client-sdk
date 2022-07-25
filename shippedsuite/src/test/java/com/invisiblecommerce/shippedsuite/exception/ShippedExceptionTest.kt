package com.invisiblecommerce.shippedsuite.exception

import com.invisiblecommerce.shippedsuite.model.ShippedError
import com.invisiblecommerce.shippedsuite.model.ShippedErrorFixtures
import org.junit.Test
import java.net.HttpURLConnection
import kotlin.test.assertEquals

class ShippedExceptionTest {

    @Test
    fun testEquals() {
        val permissionException = PermissionException(
            error = ShippedError()
        )
        assertEquals(permissionException.toString(), "com.invisiblecommerce.shippedsuite.exception.PermissionException; status-code: 403, ShippedError(message=null)")
    }
}