package com.invisiblecommerce.shippedsuite.model

interface ObjectBuilder<ObjectType> {
    fun build(): ObjectType
}
