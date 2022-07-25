package com.shippedsuite.example

import android.app.Application
import com.invisiblecommerce.shippedsuite.Mode
import com.invisiblecommerce.shippedsuite.ShippedSuite

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Setup ShippedSuite
        ShippedSuite.configurePublicKey(
            this,
            "pk_development_117c2ee46c122fb0ce070fbc984e6a4742040f05a1c73f8a900254a1933a0112"
        )

        // Optional, the default mode is development mode
        ShippedSuite.setMode(Mode.DEVELOPMENT)
    }
}
