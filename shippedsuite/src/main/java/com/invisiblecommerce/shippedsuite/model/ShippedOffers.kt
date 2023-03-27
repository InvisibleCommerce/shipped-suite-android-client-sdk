package com.invisiblecommerce.shippedsuite.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.*

@Parcelize
data class ShippedCurrency constructor(
    val decimalMark: String,

    val symbol: String,

    val symbolFirst: Boolean,

    val thousandsSeparator: String,

    val subunitToUnit: BigDecimal,

    val isoCode: String,

    val name: String
) : ShippedModel, Parcelable

@Parcelize
data class ShippedFeeWithCurrency constructor(
    val currency: ShippedCurrency,

    val subunits: BigDecimal,

    val formatted: String
) : ShippedModel, Parcelable

@Parcelize
data class ShippedOffers constructor(
    val storefrontId: String,

    val orderValue: BigDecimal,

    val shieldFee: BigDecimal?,

    val shieldFeeWithCurrency: ShippedFeeWithCurrency?,

    val greenFee: BigDecimal?,

    val greenFeeWithCurrency: ShippedFeeWithCurrency?,

    val isMandatory: Boolean,

    val offeredAt: Date
) : ShippedModel, Parcelable {

    fun isShieldAvailable(): Boolean {
        return shieldFee != null
    }

    fun isGreenAvailable(): Boolean {
        return greenFee != null
    }
}
