package com.invisiblecommerce.shippedsuite.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class LearnMoreTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val learnMoreDialog: LearnMoreDialog by lazy {
        LearnMoreDialog(context)
    }

    @Test
    fun dialogTest() {
        var configuration = ShippedSuiteConfiguration(
            type = ShippedSuiteType.SHIELD,
            isInformational = false,
            isMandatory = true,
            isRespectServer = true
        )

        learnMoreDialog.configuration = configuration
        Assert.assertEquals(learnMoreDialog.configuration.type, ShippedSuiteType.SHIELD)

        configuration.type = ShippedSuiteType.GREEN
        learnMoreDialog.configuration = configuration
        Assert.assertEquals(learnMoreDialog.configuration.type, ShippedSuiteType.GREEN)

        configuration.type = ShippedSuiteType.GREEN_AND_SHIELD
        configuration.isInformational = true
        learnMoreDialog.configuration = configuration
        Assert.assertEquals(learnMoreDialog.configuration.type, ShippedSuiteType.GREEN_AND_SHIELD)

        learnMoreDialog.configuration.type = ShippedSuiteType.GREEN_AND_SHIELD
        Assert.assertEquals(learnMoreDialog.configuration.type, ShippedSuiteType.GREEN_AND_SHIELD)

        learnMoreDialog.configuration.type = ShippedSuiteType.GREEN
        Assert.assertEquals(learnMoreDialog.configuration.type, ShippedSuiteType.GREEN)

        learnMoreDialog.configuration.type = ShippedSuiteType.SHIELD
        Assert.assertEquals(learnMoreDialog.configuration.type, ShippedSuiteType.SHIELD)
    }
}
