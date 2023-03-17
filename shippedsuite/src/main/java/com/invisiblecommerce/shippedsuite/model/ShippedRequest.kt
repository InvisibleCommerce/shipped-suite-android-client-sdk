package com.invisiblecommerce.shippedsuite.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
internal data class ShippedRequest internal constructor(

    var orderValue: BigDecimal? = null,
    var currency: String? = null

) : ShippedRequestModel, Parcelable {

    private companion object {
        private const val ORDER_VALUE = "order_value"
        private const val CURRENCY = "currency"
    }

    override fun toParamMap(): Map<String, Any> {
        return mapOf<String, Any>()
            .plus(
                orderValue?.let {
                    mapOf(ORDER_VALUE to it)
                }.orEmpty()
            )
            .plus(
                currency?.let {
                    mapOf(CURRENCY to it)
                }.orEmpty()
            )
    }

    class Builder : ObjectBuilder<ShippedRequest> {
        private var orderValue: BigDecimal? = null
        private var currency: String? = null

        fun setOrderValue(orderValue: BigDecimal?): Builder = apply {
            this.orderValue = orderValue
        }

        fun setCurrency(currency: String?): Builder = apply {
            this.currency = currency
        }

        override fun build(): ShippedRequest {
            return ShippedRequest(
                orderValue = orderValue,
                currency = currency
            )
        }
    }
}
