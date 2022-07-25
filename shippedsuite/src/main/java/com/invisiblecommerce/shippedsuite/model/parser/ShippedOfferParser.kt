package com.invisiblecommerce.shippedsuite.model.parser

import com.invisiblecommerce.shippedsuite.model.ShippedOffers
import org.json.JSONObject
import java.math.BigDecimal

class ShippedOffersParser : ModelJsonParser<ShippedOffers> {

    override fun parse(json: JSONObject): ShippedOffers {
        return ShippedOffers(
            storefrontId = json.getString(STOREFRONT_ID),
            orderValue = BigDecimal.valueOf(json.getDouble(ORDER_VALUE)),
            shieldFee = BigDecimal.valueOf(json.getDouble(SHIELD_FEE)),
            greenFee = BigDecimal.valueOf(json.getDouble(GREEN_FEE)),
            offeredAt = dateFormat.parse(json.getString(OFFERED_AT))
        )
    }

    companion object {
        const val STOREFRONT_ID = "storefront_id"
        const val ORDER_VALUE = "order_value"
        const val SHIELD_FEE = "shield_fee"
        const val GREEN_FEE = "green_fee"
        const val OFFERED_AT = "offered_at"
    }
}
