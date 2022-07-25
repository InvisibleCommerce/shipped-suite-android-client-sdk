package com.invisiblecommerce.shippedsuite

import com.invisiblecommerce.shippedsuite.model.Options
import com.invisiblecommerce.shippedsuite.model.ShippedOffers

interface APIRepository {

    suspend fun getOffersFee(
        options: Options
    ): ShippedOffers
}
