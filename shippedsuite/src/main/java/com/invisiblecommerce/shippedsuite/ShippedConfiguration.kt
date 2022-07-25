package com.invisiblecommerce.shippedsuite

import android.content.Context

/**
`ShippedConfiguration` contains the base configuration the SDK needs.
 */
data class ShippedConfiguration internal constructor(
    val applicationContext: Context,
    val publicKey: String,
    var enableLogging: Boolean,
    var environment: Mode
) {
    class Builder(private val applicationContext: Context, private val publicKey: String) {

        private var enableLogging: Boolean = false

        private var environment: Mode = Mode.DEVELOPMENT

        fun enableLogging(enable: Boolean): Builder = apply {
            this.enableLogging = enable
        }

        fun setEnvironment(environment: Mode): Builder = apply {
            this.environment = environment
        }

        fun build(): ShippedConfiguration {
            return ShippedConfiguration(
                applicationContext = applicationContext,
                publicKey = publicKey,
                enableLogging = enableLogging,
                environment = environment
            )
        }
    }
}
