package com.invisiblecommerce.shippedsuite.model.parser

import com.invisiblecommerce.shippedsuite.model.ShippedError
import com.invisiblecommerce.shippedsuite.util.JsonUtils
import org.json.JSONObject

class ShippedErrorParser : ModelJsonParser<ShippedError> {

    override fun parse(json: JSONObject): ShippedError {
        return ShippedError(
            message = JsonUtils.optString(json, FIELD_ERROR)
        )
    }

    private companion object {
        private const val FIELD_ERROR = "error"
    }
}
