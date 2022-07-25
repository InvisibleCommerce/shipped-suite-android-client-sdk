package com.invisiblecommerce.shippedsuite.exception

import com.invisiblecommerce.shippedsuite.model.ShippedError

class APIException(
    error: ShippedError? = null,
    statusCode: Int = 0,
    message: String? = error?.message,
    e: Throwable? = null
) : ShippedException(error, statusCode, message, e)
