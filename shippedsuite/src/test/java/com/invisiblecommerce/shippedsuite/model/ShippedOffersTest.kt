package com.invisiblecommerce.shippedsuite.model

import org.junit.Test
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals

class ShippedOffersTest {

    private val date = Date()

    private val offer = ShippedOffers(
        storefrontId = "1",
        orderValue = BigDecimal.valueOf(129.99),
        shieldFee = BigDecimal.valueOf(2.27),
        greenFee = BigDecimal.valueOf(0.39),
        offeredAt = date
    )

    @Test
    fun testParams() {
        assertEquals("1", offer.storefrontId)
        assertEquals(BigDecimal.valueOf(129.99), offer.orderValue)
        assertEquals(BigDecimal.valueOf(2.27), offer.shieldFee)
        assertEquals(BigDecimal.valueOf(0.39), offer.greenFee)
        assertEquals(date, offer.offeredAt)
    }
}
