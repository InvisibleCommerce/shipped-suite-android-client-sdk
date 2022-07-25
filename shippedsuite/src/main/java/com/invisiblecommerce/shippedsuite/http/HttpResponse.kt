package com.invisiblecommerce.shippedsuite.http

import com.invisiblecommerce.shippedsuite.exception.APIException
import org.json.JSONException
import org.json.JSONObject

data class HttpResponse internal constructor(
    val code: Int,
    internal val body: String?,
    internal val headers: Map<String, List<String>> = emptyMap()
) {
    val isError: Boolean = code < 200 || code >= 300

    val responseJson: JSONObject
        @Throws(APIException::class)
        get() {
            return body?.let {
                try {
                    JSONObject(it)
                } catch (e: JSONException) {
                    throw APIException(
                        message = "Exception while parsing response body. Status code: $code",
                        e = e
                    )
                }
            } ?: JSONObject()
        }

    private fun String.getHeaderValue(): List<String>? {
        return headers.entries.firstOrNull {
            it.key.equals(this, ignoreCase = true)
        }?.value
    }

    override fun toString(): String {
        return "Status Code: $code \n $body"
    }
}
