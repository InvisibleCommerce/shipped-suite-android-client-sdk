package com.invisiblecommerce.shippedsuite

import com.invisiblecommerce.shippedsuite.model.Options

interface OperationManager {

    fun <T> startOperation(options: Options, listener: ShippedSuite.Listener<T>)
}
