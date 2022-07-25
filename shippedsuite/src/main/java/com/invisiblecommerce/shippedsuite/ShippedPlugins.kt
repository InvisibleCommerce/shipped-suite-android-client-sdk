package com.invisiblecommerce.shippedsuite

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

internal object ShippedPlugins {

    private const val SHARED_PREF = "shipped_sp"
    private const val WIDGET_VIEW_IS_SELECTED = "widget_view_is_selected"

    private lateinit var configuration: ShippedConfiguration

    private lateinit var sp: SharedPreferences

    fun initialize(configuration: ShippedConfiguration) {
        this.configuration = configuration
        this.sp = configuration.applicationContext.getSharedPreferences(
            SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    internal val publicKey: String
        get() {
            return configuration.publicKey
        }

    internal var enableLogging: Boolean
        set(value) {
            configuration.enableLogging = value
        }
        get() {
            return configuration.enableLogging
        }

    internal var environment: Mode
        set(value) {
            configuration.environment = value
        }
        get() {
            return configuration.environment
        }

    internal var widgetViewIsSelected: Boolean
        get() {
            return sp.getBoolean(WIDGET_VIEW_IS_SELECTED, true)
        }
        set(value) {
            sp.edit {
                putBoolean(WIDGET_VIEW_IS_SELECTED, value)
            }
        }
}
