package com.invisiblecommerce.shippedsuite.exception

import com.invisiblecommerce.shippedsuite.model.ShippedError
import java.io.IOException

class APIConnectionException(
    message: String? = null,
    e: Throwable
) : ShippedException(ShippedError(message = message), STATUS_CODE, message, e) {
    companion object {
        private const val STATUS_CODE = 0

        @JvmSynthetic
        fun create(e: IOException, url: String? = null): APIConnectionException {
            return APIConnectionException(
                "IOException during API request to $url: ${e.message}. Please check your internet connection and try again.",
                e
            )
        }
    }
}
