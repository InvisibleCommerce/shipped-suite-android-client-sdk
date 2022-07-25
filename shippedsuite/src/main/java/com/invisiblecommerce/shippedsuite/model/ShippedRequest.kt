package com.invisiblecommerce.shippedsuite.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
internal data class ShippedRequest internal constructor(

    var orderValue: BigDecimal? = null
) : ShippedRequestModel, Parcelable {

    private companion object {
        private const val ORDER_VALUE = "order_value"
    }

    override fun toParamMap(): Map<String, Any> {
        return mapOf<String, Any>()
            .plus(
                orderValue?.let {
                    mapOf(ORDER_VALUE to it)
                }.orEmpty()
            )
    }

    class Builder : ObjectBuilder<ShippedRequest> {
        private var orderValue: BigDecimal? = null

        fun setOrderValue(orderValue: BigDecimal?): Builder = apply {
            this.orderValue = orderValue
        }

        override fun build(): ShippedRequest {
            return ShippedRequest(
                orderValue = orderValue
            )
        }
    }
}
