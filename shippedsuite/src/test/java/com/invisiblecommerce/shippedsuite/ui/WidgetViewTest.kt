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

        val defaultOrderValue = BigDecimal.valueOf(129.99)
        widgetView.updateOrderValue(defaultOrderValue)

        widgetView.type = ShippedSuiteType.GREEN_AND_SHIELD
        Assert.assertEquals(widgetView.type, ShippedSuiteType.GREEN_AND_SHIELD)

        widgetView.type = ShippedSuiteType.GREEN
        Assert.assertEquals(widgetView.type, ShippedSuiteType.GREEN)

        widgetView.type = ShippedSuiteType.SHIELD
        Assert.assertEquals(widgetView.type, ShippedSuiteType.SHIELD)

        widgetView.isMandatory = true
        Assert.assertEquals(widgetView.isMandatory, true)

        widgetView.isRespectServer = true
        Assert.assertEquals(widgetView.isRespectServer, true)
    }
}
