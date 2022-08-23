package com.invisiblecommerce.shippedsuite.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.*

@Parcelize
data class ShippedOffers constructor(
    val storefrontId: String,

    val orderValue: BigDecimal,

    val shieldFee: BigDecimal?,

    val greenFee: BigDecimal?,

    val offeredAt: Date
) : ShippedModel, Parcelable
