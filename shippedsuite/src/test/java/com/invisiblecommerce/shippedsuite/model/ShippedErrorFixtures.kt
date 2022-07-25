package com.invisiblecommerce.shippedsuite.model

import com.invisiblecommerce.shippedsuite.model.parser.ShippedErrorParser
import org.json.JSONObject

internal object ShippedErrorFixtures {

    val Error: ShippedError = ShippedErrorParser().parse(
        JSONObject(
            """
        {
            "error": "Auth error"
        }
            """.trimIndent()
        )
    )
}
