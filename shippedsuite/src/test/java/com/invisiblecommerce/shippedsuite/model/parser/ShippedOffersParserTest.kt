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
                "mandatory": true,
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
        assertEquals(offer.isMandatory, true)
        assertEquals(offer.offeredAt, parser.dateFormat.parse("2022-05-18T18:03:22.252-07:00"))
    }

    @Test
    fun currencyParserTest() {
        val jsonObject = JSONObject(
            """
                {
                "decimal_mark": ".",
                "symbol": "$",
                "symbol_first": true,
                "thousands_separator": ",",
                "subunit_to_unit": 100,
                "iso_code": "USD",
                "name": "United States Dollar"
                }
            """.trimIndent()
        )

        val parser = ShippedCurrencyParser()
        assertNotNull(parser.dateFormat)

        val currency = parser.parse(jsonObject)
        assertEquals(currency.decimalMark, ".")
        assertEquals(currency.symbol, "$")
        assertEquals(currency.symbolFirst, true)
        assertEquals(currency.thousandsSeparator, ",")
        assertEquals(currency.subunitToUnit, BigDecimal.valueOf(100.0))
        assertEquals(currency.isoCode, "USD")
        assertEquals(currency.name, "United States Dollar")
    }

    @Test
    fun feeWithCurrencyParserTest() {
        val jsonObject = JSONObject(
            """
                {
                "currency": {
                    "decimal_mark": ".",
                    "symbol": "$",
                    "symbol_first": true,
                    "thousands_separator": ",",
                    "subunit_to_unit": 100,
                    "iso_code": "USD",
                    "name": "United States Dollar"
                },
                "subunits": 277,
                "formatted": "$2.77"
                }
            """.trimIndent()
        )

        val parser = ShippedFeeWithCurrencyParser()
        assertNotNull(parser.dateFormat)

        val feeWithCurrency = parser.parse(jsonObject)
        assertNotNull(feeWithCurrency.currency)
        assertEquals(feeWithCurrency.subunits, BigDecimal.valueOf(277.0))
        assertEquals(feeWithCurrency.formatted, "$2.77")
    }

    @Test
    fun switchBuildVersion() {
        setFinalStatic(VERSION::class.java.getField("SDK_INT"), 25)

        val jsonObject = JSONObject(
            """
                {
                "storefront_id": "test-paws.myshopify.com",
                "order_value": "129.99",
                "shield_fee": "2.27",
                "green_fee": "0.39",
                "mandatory": true,
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
        assertEquals(offer.isMandatory, true)
        assertEquals(offer.offeredAt, parser.dateFormat.parse("2022-05-18T18:03:22.252-07:00"))
    }
}
