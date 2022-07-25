package com.invisiblecommerce.shippedsuite.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class ShippedError(

    /**
     * Error message
     */
    val message: String? = null
) : ShippedModel, Parcelable, Serializable
