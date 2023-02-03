package com.invisiblecommerce.shippedsuite.model.parser

import com.invisiblecommerce.shippedsuite.model.ShippedOffers
import com.invisiblecommerce.shippedsuite.util.JsonUtils
import org.json.JSONObject
import java.math.BigDecimal

class ShippedOffersParser : ModelJsonParser<ShippedOffers> {

    override fun parse(json: JSONObject): ShippedOffers {
        return ShippedOffers(
            storefrontId = json.optString(STOREFRONT_ID),
            orderValue = BigDecimal.valueOf(json.getDouble(ORDER_VALUE)),
            shieldFee = JsonUtils.optDouble(json, SHIELD_FEE)?.let {
                BigDecimal.valueOf(it)
            },
            greenFee = JsonUtils.optDouble(json, GREEN_FEE)?.let {
                BigDecimal.valueOf(it)
            },
            isMandatory = JsonUtils.optBoolean(json, IS_MANDATORY),
            offeredAt = dateFormat.parse(json.optString(OFFERED_AT))
        )
    }

    companion object {
        const val STOREFRONT_ID = "storefront_id"
        const val ORDER_VALUE = "order_value"
        const val SHIELD_FEE = "shield_fee"
        const val GREEN_FEE = "green_fee"
        const val IS_MANDATORY = "mandatory"
        const val OFFERED_AT = "offered_at"
    }
}
