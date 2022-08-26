package com.invisiblecommerce.shippedsuite

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class ShippedConfigurationTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun configureTest() {
        val publicKey =
            "pk_development_117c2ee46c122fb0ce070fbc984e6a4742040f05a1c73f8a900254a1933a0112"

        val configuration = ShippedConfiguration.Builder(context, publicKey)
            .enableLogging(true)
            .setEnvironment(Mode.DEVELOPMENT)
            .build()

        Assert.assertEquals(configuration.publicKey, publicKey)
        Assert.assertEquals(configuration.enableLogging, true)
        Assert.assertEquals(configuration.environment, Mode.DEVELOPMENT)
    }
}
