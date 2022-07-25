package com.invisiblecommerce.shippedsuite.model

interface ShippedModel {
    override fun hashCode(): Int

    override fun equals(other: Any?): Boolean
}
