package com.invisiblecommerce.shippedsuite

import android.content.Context
import com.invisiblecommerce.shippedsuite.exception.ShippedException
import com.invisiblecommerce.shippedsuite.model.ShippedOffers
import com.invisiblecommerce.shippedsuite.model.ShippedRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.math.BigDecimal

/**
`ShippedSuite` contains all the configuration and functionality provided by the SDK.
 */
class ShippedSuite internal constructor(
    private val operationManager: OperationManager,
) {

    interface Listener<T> {
        fun onSuccess(response: T)
        fun onFailed(exception: ShippedException)
    }

    constructor(ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : this(
        ShippedOperationManager(ShippedAPIRepository(ioDispatcher)),
    )

    /**
    Get offers fee.
    @param orderValue An order value.
    @param currency A currency code.
    @param listener A handler which includes shield & green fee.
     */
    fun getOffersFee(
        orderValue: BigDecimal,
        currency: String? = null,
        listener: Listener<ShippedOffers>
    ) {
        operationManager.startOperation(
            ShippedAPIRepository.ShippedRequestOptions(
                request = ShippedRequest.Builder().setOrderValue(orderValue).setCurrency(currency).build()
            ),
            listener
        )
    }

    companion object {

        /**
         Configure public key.
         */
        fun configurePublicKey(applicationContext: Context, publicKey: String) {
            ShippedPlugins.initialize(
                ShippedConfiguration.Builder(applicationContext, publicKey)
                    .enableLogging(false)
                    .setEnvironment(Mode.DEVELOPMENT)
                    .build()
            )
        }

        /**
         Enable logging. False as default.
         */
        fun enableLogging(enable: Boolean) {
            ShippedPlugins.enableLogging = enable
        }

        /**
         Get sdk mode. Development mode as default.
         */
        fun setMode(environment: Mode) {
            ShippedPlugins.environment = environment
        }
    }
}
