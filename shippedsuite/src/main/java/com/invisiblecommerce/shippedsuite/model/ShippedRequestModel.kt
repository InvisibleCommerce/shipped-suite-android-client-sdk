package com.invisiblecommerce.shippedsuite.model

interface ShippedRequestModel {
    fun toParamMap(): Map<String, Any>
}
