package com.invisiblecommerce.shippedsuite.model.parser

import android.os.Build.VERSION
import org.json.JSONObject
import org.junit.Test
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ShippedOffersParserTest {

    @Throws(Exception::class)
    fun setFinalStatic(field: Field, newValue: Any?) {
        field.setAccessible(true)
        val modifiersField: Field = Field::class.java.getDeclaredField("modifiers")
        modifiersField.setAccessible(true)
        modifiersField.setInt(field, field.getModifiers() and Modifier.FINAL.inv())
        field.set(null, newValue)
    }

    @Test
    fun parserTest() {
        val jsonObject = JSONObject(
            """
                {
                "storefront_id": "test-paws.myshopify.com",
                "order_value": "129.99",
                "shield_fee": "2.27",
                "green_fee": "0.39",
                "offered_at": "2022-05-18T18:03:22.252-07:00"
                }
            """.trimIndent()
        )

        val parser = ShippedOffersParser()
        assertNotNull(parser.dateFormat)

        val offer = parser.parse(jsonObject)
        assertEquals(offer.storefrontId, "test-paws.myshopify.com")
        assertEquals(offer.orderValue, BigDecimal.valueOf(129.99))
        assertEquals(offer.shieldFee, BigDecimal.valueOf(2.27))
        assertEquals(offer.greenFee, BigDecimal.valueOf(0.39))
        assertEquals(offer.offeredAt, parser.dateFormat.parse("2022-05-18T18:03:22.252-07:00"))
    }

    @Test
    fun switchBuldVersion() {
        setFinalStatic(VERSION::class.java.getField("SDK_INT"), 25)

        val jsonObject = JSONObject(
            """
                {
                "storefront_id": "test-paws.myshopify.com",
                "order_value": "129.99",
                "shield_fee": "2.27",
                "green_fee": "0.39",
                "offered_at": "2022-05-18T18:03:22.252-07:00"
                }
            """.trimIndent()
        )

        val parser = ShippedOffersParser()
        assertNotNull(parser.dateFormat)

        val offer = parser.parse(jsonObject)
        assertEquals(offer.storefrontId, "test-paws.myshopify.com")
        assertEquals(offer.orderValue, BigDecimal.valueOf(129.99))
        assertEquals(offer.shieldFee, BigDecimal.valueOf(2.27))
        assertEquals(offer.greenFee, BigDecimal.valueOf(0.39))
        assertEquals(offer.offeredAt, parser.dateFormat.parse("2022-05-18T18:03:22.252-07:00"))
    }
}
