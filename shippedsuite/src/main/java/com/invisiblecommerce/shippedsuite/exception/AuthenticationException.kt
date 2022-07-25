package com.invisiblecommerce.shippedsuite.exception

import com.invisiblecommerce.shippedsuite.model.ShippedError
import java.net.HttpURLConnection

class AuthenticationException(
    error: ShippedError
) : ShippedException(error, HttpURLConnection.HTTP_UNAUTHORIZED)
