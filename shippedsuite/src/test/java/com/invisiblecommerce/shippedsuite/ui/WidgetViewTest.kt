package com.invisiblecommerce.shippedsuite.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.invisiblecommerce.shippedsuite.ShippedSuite
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class WidgetViewTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val widgetView: WidgetView by lazy {
        WidgetView(context, null)
    }

    @Test
    fun widgetTest() {
        val publicKey =
            "pk_development_117c2ee46c122fb0ce070fbc984e6a4742040f05a1c73f8a900254a1933a0112"
        ShippedSuite.configurePublicKey(context, publicKey)

        var configuration = ShippedSuiteConfiguration(
            type = ShippedSuiteType.SHIELD,
            isInformational = false,
            isMandatory = true,
            isRespectServer = true,
            appearance = ShippedSuiteAppearance.LIGHT
        )
        widgetView.configuration = configuration

        configuration.type = ShippedSuiteType.GREEN
        configuration.isMandatory = false
        widgetView.configuration = configuration
        Assert.assertEquals(widgetView.configuration.type, ShippedSuiteType.GREEN)

        configuration.type = ShippedSuiteType.GREEN_AND_SHIELD
        configuration.isInformational = true
        widgetView.configuration = configuration
        Assert.assertEquals(widgetView.configuration.type, ShippedSuiteType.GREEN_AND_SHIELD)

        val defaultOrderValue = BigDecimal.valueOf(129.99)
        widgetView.updateOrderValue(defaultOrderValue)

        widgetView.configuration.type = ShippedSuiteType.GREEN_AND_SHIELD
        Assert.assertEquals(widgetView.configuration.type, ShippedSuiteType.GREEN_AND_SHIELD)

        widgetView.configuration.type = ShippedSuiteType.GREEN
        Assert.assertEquals(widgetView.configuration.type, ShippedSuiteType.GREEN)

        widgetView.configuration.type = ShippedSuiteType.SHIELD
        Assert.assertEquals(widgetView.configuration.type, ShippedSuiteType.SHIELD)

        widgetView.configuration.isMandatory = true
        Assert.assertEquals(widgetView.configuration.isMandatory, true)

        widgetView.configuration.isRespectServer = true
        Assert.assertEquals(widgetView.configuration.isRespectServer, true)
    }

    @Test
    fun configurationTest() {
        val configuration = ShippedSuiteConfiguration()
        Assert.assertEquals(configuration.type, ShippedSuiteType.SHIELD)
        Assert.assertEquals(configuration.isMandatory, false)
        Assert.assertEquals(configuration.isInformational, false)
        Assert.assertEquals(configuration.isRespectServer, false)
        Assert.assertEquals(configuration.appearance, ShippedSuiteAppearance.AUTO)

        configuration.type = ShippedSuiteType.GREEN
        Assert.assertEquals(configuration.type, ShippedSuiteType.GREEN)

        configuration.isMandatory = true
        Assert.assertEquals(configuration.isMandatory, true)

        configuration.isInformational = true
        Assert.assertEquals(configuration.isInformational, true)

        configuration.isRespectServer = true
        Assert.assertEquals(configuration.isRespectServer, true)

        configuration.appearance = ShippedSuiteAppearance.DARK
        Assert.assertEquals(configuration.appearance, ShippedSuiteAppearance.DARK)
    }
}
