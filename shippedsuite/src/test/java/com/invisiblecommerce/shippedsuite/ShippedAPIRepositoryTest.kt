package com.invisiblecommerce.shippedsuite

import com.invisiblecommerce.shippedsuite.model.ShippedRequest
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal

class ShippedAPIRepositoryTest {

    @Test
    fun createOffersFeeRequestTest() {
        val defaultOrderValue = BigDecimal.valueOf(129.99)
        val request = ShippedRequest.Builder().setOrderValue(defaultOrderValue).build()
        Assert.assertNotEquals(request, null)
        Assert.assertNotEquals(request.toParamMap(), null)
        Assert.assertEquals(request.orderValue, defaultOrderValue)

        val url = ShippedAPIRepository.createShippedOffersUrl(Mode.DEVELOPMENT.baseUrl())
        Assert.assertNotEquals(url, null)

        val options = ShippedAPIRepository.ShippedRequestOptions(request)
        Assert.assertEquals(options.request, request)
    }
}
