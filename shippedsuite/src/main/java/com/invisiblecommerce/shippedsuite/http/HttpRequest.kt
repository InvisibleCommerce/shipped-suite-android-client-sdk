package com.invisiblecommerce.shippedsuite.http

import com.invisiblecommerce.shippedsuite.ShippedPlugins
import com.invisiblecommerce.shippedsuite.exception.InvalidRequestException
import com.invisiblecommerce.shippedsuite.util.JsonUtils
import java.io.OutputStream
import java.io.UnsupportedEncodingException

open class HttpRequest internal constructor(
    val method: Method,
    val url: String,
    private val params: Map<String, *>? = null,
    private val accept: String? = null
) {
    private val mimeType: MimeType = MimeType.Json

    internal open val contentType: String
        get() {
            return "${mimeType.code}; charset=$CHARSET"
        }

    internal val headers: Map<String, String>
        get() {
            return mapOf(
                ACCEPT_HEADER_KEY to (accept ?: ACCEPT_HEADER_VALUE),
                CONTENT_TYPE_HEADER_KEY to CONTENT_TYPE_HEADER_VALUE
            )
                .plus(
                    if (ShippedPlugins.publicKey.isEmpty()) {
                        emptyMap()
                    } else {
                        mapOf(AUTHORIZATION to "Bearer ${ShippedPlugins.publicKey}")
                    }
                )
        }

    private val body: String
        @Throws(InvalidRequestException::class, UnsupportedEncodingException::class)
        get() {
            return JsonUtils.mapToJsonObject(params).toString()
        }

    internal open fun writeBody(outputStream: OutputStream) {
        try {
            body.toByteArray(Charsets.UTF_8).let {
                outputStream.write(it)
                outputStream.flush()
            }
        } catch (e: UnsupportedEncodingException) {
            throw InvalidRequestException(
                message = "Unable to encode parameters to ${Charsets.UTF_8.name()}."
            )
        }
    }

    enum class Method(val code: String) {
        GET("GET"),
        POST("POST")
    }

    internal enum class MimeType(val code: String) {
        Json("application/json")
    }

    override fun toString(): String {
        return if (Method.POST == method) {
            "${method.code} $url \n $body"
        } else {
            "${method.code} $url"
        }
    }

    companion object {
        private val CHARSET = Charsets.UTF_8.name()
        private const val ACCEPT_HEADER_KEY = "Accept"
        private const val ACCEPT_HEADER_VALUE = "application/json"
        private const val CONTENT_TYPE_HEADER_KEY = "Content-Type"
        private const val CONTENT_TYPE_HEADER_VALUE = "application/json"
        private const val AUTHORIZATION = "Authorization"

        fun createGet(
            url: String,
            params: Map<String, *>? = null,
            accept: String? = null
        ): HttpRequest {
            return HttpRequest(Method.GET, url, params, accept)
        }

        fun createPost(
            url: String,
            params: Map<String, *>? = null
        ): HttpRequest {
            return HttpRequest(Method.POST, url, params)
        }
    }
}
