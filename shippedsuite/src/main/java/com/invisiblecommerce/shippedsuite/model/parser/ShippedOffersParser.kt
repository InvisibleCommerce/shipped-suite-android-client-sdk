package com.invisiblecommerce.shippedsuite.model.parser

import com.invisiblecommerce.shippedsuite.model.ShippedCurrency
import com.invisiblecommerce.shippedsuite.model.ShippedFeeWithCurrency
import com.invisiblecommerce.shippedsuite.model.ShippedOffers
import com.invisiblecommerce.shippedsuite.util.JsonUtils
import org.json.JSONObject
import java.math.BigDecimal

class ShippedCurrencyParser : ModelJsonParser<ShippedCurrency> {

    override fun parse(json: JSONObject): ShippedCurrency {

        return ShippedCurrency(
            decimalMark = json.optString(DECIMAL_MARK),
            symbol = json.optString(SYMBOL),
            symbolFirst = json.optBoolean(SYMBOL_FIRST),
            thousandsSeparator = json.optString(THOUSANDS_SEPARATOR),
            subunitToUnit = BigDecimal.valueOf(json.getDouble(SUBUNIT_TO_UNIT)),
            isoCode = json.optString(ISO_CODE),
            name = json.optString(NAME)
        )
    }

    companion object {
        const val DECIMAL_MARK = "decimal_mark"
        const val SYMBOL = "symbol"
        const val SYMBOL_FIRST = "symbol_first"
        const val THOUSANDS_SEPARATOR = "thousands_separator"
        const val SUBUNIT_TO_UNIT = "subunit_to_unit"
        const val ISO_CODE = "iso_code"
        const val NAME = "name"
    }
}

class ShippedFeeWithCurrencyParser : ModelJsonParser<ShippedFeeWithCurrency> {

    override fun parse(json: JSONObject): ShippedFeeWithCurrency {
        return ShippedFeeWithCurrency(
            currency = ShippedCurrencyParser().parse(json.getJSONObject(CURRENCY)),
            subunits = BigDecimal.valueOf(json.getDouble(SUBUNITS)),
            formatted = json.optString(FORMATTED),
        )
    }

    companion object {
        const val CURRENCY = "currency"
        const val SUBUNITS = "subunits"
        const val FORMATTED = "formatted"
    }
}

class ShippedOffersParser : ModelJsonParser<ShippedOffers> {

    override fun parse(json: JSONObject): ShippedOffers {
        return ShippedOffers(
            storefrontId = json.optString(STOREFRONT_ID),
            orderValue = BigDecimal.valueOf(json.getDouble(ORDER_VALUE)),
            shieldFee = JsonUtils.optDouble(json, SHIELD_FEE)?.let {
                BigDecimal.valueOf(it)
            },
            shieldFeeWithCurrency = json.optJSONObject(SHIELD_FEE_WITH_CURRENCY)?.let {
                ShippedFeeWithCurrencyParser().parse(it)
            },
            greenFee = JsonUtils.optDouble(json, GREEN_FEE)?.let {
                BigDecimal.valueOf(it)
            },
            greenFeeWithCurrency = json.optJSONObject(GREEN_FEE_WITH_CURRENCY)?.let {
                ShippedFeeWithCurrencyParser().parse(it)
            },
            isMandatory = JsonUtils.optBoolean(json, IS_MANDATORY),
            offeredAt = dateFormat.parse(json.optString(OFFERED_AT))
        )
    }

    companion object {
        const val STOREFRONT_ID = "storefront_id"
        const val ORDER_VALUE = "order_value"
        const val SHIELD_FEE = "shield_fee"
        const val SHIELD_FEE_WITH_CURRENCY = "shield_fee_with_currency"
        const val GREEN_FEE = "green_fee"
        const val GREEN_FEE_WITH_CURRENCY = "green_fee_with_currency"
        const val IS_MANDATORY = "mandatory"
        const val OFFERED_AT = "offered_at"
    }
}
