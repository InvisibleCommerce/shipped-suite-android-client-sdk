package com.invisiblecommerce.shippedsuite

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.invisiblecommerce.shippedsuite.exception.ShippedException
import com.invisiblecommerce.shippedsuite.model.ShippedOffers
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class ShippedSuiteTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun beforeTest(){
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun afterTest(){
        Dispatchers.resetMain()
    }

    @Test
    fun getOffersFeeTest() = runTest {
        val publicKey =
            "pk_development_117c2ee46c122fb0ce070fbc984e6a4742040f05a1c73f8a900254a1933a0112"
        ShippedSuite.configurePublicKey(context, publicKey)
        ShippedSuite.enableLogging(true)
        ShippedSuite.setMode(Mode.DEVELOPMENT)

        val defaultOrderValue = BigDecimal.valueOf(129.99)
        ShippedSuite(UnconfinedTestDispatcher()).getOffersFee(
            defaultOrderValue,
            object : ShippedSuite.Listener<ShippedOffers> {
                override fun onSuccess(response: ShippedOffers) {
                    assert(true)
                }

                override fun onFailed(exception: ShippedException) {
                    assert(false)
                }
            }
        )
    }
}
