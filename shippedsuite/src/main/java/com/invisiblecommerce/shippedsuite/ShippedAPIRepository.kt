package com.invisiblecommerce.shippedsuite

import android.os.Parcelable
import com.invisiblecommerce.shippedsuite.exception.*
import com.invisiblecommerce.shippedsuite.http.HttpClient
import com.invisiblecommerce.shippedsuite.http.HttpRequest
import com.invisiblecommerce.shippedsuite.http.HttpResponse
import com.invisiblecommerce.shippedsuite.model.Options
import com.invisiblecommerce.shippedsuite.model.ShippedModel
import com.invisiblecommerce.shippedsuite.model.ShippedOffers
import com.invisiblecommerce.shippedsuite.model.ShippedRequest
import com.invisiblecommerce.shippedsuite.model.parser.ModelJsonParser
import com.invisiblecommerce.shippedsuite.model.parser.ShippedErrorParser
import com.invisiblecommerce.shippedsuite.model.parser.ShippedOffersParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import java.io.IOException
import java.net.HttpURLConnection
import java.util.*

internal class ShippedAPIRepository(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    APIRepository {

    private val httpClient: HttpClient = HttpClient()

    @Parcelize
    class ShippedRequestOptions constructor(
        val request: ShippedRequest
    ) : Options, Parcelable

    /**
     The request of getting offers fee.
     */
    override suspend fun getOffersFee(options: Options): ShippedOffers {
        val baseUrl: String = ShippedPlugins.environment.baseUrl()

        return executeApiRequest(
            HttpRequest.createPost(
                url = createShippedOffersUrl(baseUrl),
                params = (options as ShippedRequestOptions).request.toParamMap()
            ),
            ShippedOffersParser()
        )
    }

    @Throws(
        AuthenticationException::class,
        InvalidRequestException::class,
        PermissionException::class,
        APIException::class,
        APIConnectionException::class
    )
    private suspend fun <ModelType : ShippedModel> executeApiRequest(
        request: HttpRequest,
        jsonParser: ModelJsonParser<ModelType>
    ): ModelType = withContext(ioDispatcher) {
        val response = runCatching {
            httpClient.execute(request)
        }.getOrElse {
            throw when (it) {
                is IOException -> APIConnectionException.create(it, request.url)
                else -> it
            }
        }

        if (response.isError) {
            handleApiError(response)
        }

        jsonParser.parse(response.responseJson)
    }

    @Throws(
        AuthenticationException::class,
        InvalidRequestException::class,
        PermissionException::class,
        APIException::class
    )
    private fun handleApiError(response: HttpResponse) {
        val responseCode = response.code
        val error = ShippedErrorParser().parse(response.responseJson)
        when (responseCode) {
            HttpURLConnection.HTTP_BAD_REQUEST, HttpURLConnection.HTTP_NOT_FOUND -> {
                throw InvalidRequestException(error = error)
            }
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                throw AuthenticationException(error = error)
            }
            HttpURLConnection.HTTP_FORBIDDEN -> {
                throw PermissionException(error = error)
            }
            else -> {
                throw APIException(error = error, statusCode = responseCode)
            }
        }
    }

    companion object {
        internal fun createShippedOffersUrl(baseUrl: String): String {
            return getApiUrl(
                baseUrl,
                "v1/offers"
            )
        }

        @Suppress("DEPRECATION")
        internal fun getApiUrl(baseUrl: String, path: String, vararg args: Any): String {
            return "$baseUrl/${String.format(Locale.ENGLISH, path, *args)}"
        }
    }
}
