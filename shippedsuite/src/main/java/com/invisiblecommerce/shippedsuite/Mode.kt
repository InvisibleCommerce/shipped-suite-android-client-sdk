package com.invisiblecommerce.shippedsuite

enum class Mode(val value: String) {
    DEVELOPMENT("development"), PRODUCTION("production");

    fun baseUrl(): String {
        return when (this) {
            DEVELOPMENT -> "https://api-staging.shippedsuite.com"
            PRODUCTION -> "https://api.shippedsuite.com"
        }
    }
}
