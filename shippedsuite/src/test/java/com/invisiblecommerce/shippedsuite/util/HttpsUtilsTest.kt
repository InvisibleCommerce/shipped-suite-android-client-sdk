package com.invisiblecommerce.shippedsuite.util

import com.invisiblecommerce.shippedsuite.Mode
import org.junit.Test
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.test.assertNotNull

class HttpsUtilsTest {

    @Test
    fun httpsTest() {
        val connection = URL(Mode.DEVELOPMENT.baseUrl()).openConnection()
        assertNotNull(connection)

        val unsafeConnection = (connection as HttpsURLConnection).unsafe()
        assertNotNull(unsafeConnection)
    }
}
