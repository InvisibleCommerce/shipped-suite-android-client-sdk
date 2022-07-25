package com.invisiblecommerce.shippedsuite.exception

import com.invisiblecommerce.shippedsuite.model.ShippedError

abstract class ShippedException @JvmOverloads constructor(
    val error: ShippedError?,
    private val statusCode: Int,
    message: String? = error?.message,
    e: Throwable? = null
) : Exception(message, e) {

    override fun toString(): String {
        val statusCodeStr = "; status-code: $statusCode"
        return "${super.toString() + statusCodeStr}, $error"
    }
}
