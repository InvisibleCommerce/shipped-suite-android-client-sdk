package com.invisiblecommerce.shippedsuite

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class ShippedPluginsTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun configureTest() {
        val publicKey =
            "pk_development_117c2ee46c122fb0ce070fbc984e6a4742040f05a1c73f8a900254a1933a0112"
        ShippedPlugins.initialize(
            ShippedConfiguration.Builder(context, publicKey)
                .enableLogging(false)
                .setEnvironment(Mode.DEVELOPMENT)
                .build()
        )

        assertEquals(ShippedPlugins.publicKey, publicKey)
        assertFalse(ShippedPlugins.enableLogging)
        assertEquals(ShippedPlugins.environment, Mode.DEVELOPMENT)

        ShippedPlugins.widgetViewIsSelected = true
        assertTrue(ShippedPlugins.widgetViewIsSelected)

        val configuration = ShippedConfiguration(context, publicKey, true, Mode.PRODUCTION)
        assertEquals(configuration.publicKey, publicKey)
        assertEquals(configuration.enableLogging, true)
        assertEquals(configuration.environment, Mode.PRODUCTION)
    }
}
