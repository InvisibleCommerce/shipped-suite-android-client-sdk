package com.invisiblecommerce.shippedsuite.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.invisiblecommerce.shippedsuite.ShippedSuite
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class LearnMoreTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val learnMoreDialog: LearnMoreDialog by lazy {
        LearnMoreDialog(context)
    }

    @Test
    fun dialogTest() {
        learnMoreDialog
    }
}
