package com.astro.test.aqil.features.dashboard.utils

import android.content.Context
import com.astro.R

/**
 * to access resource in viewmodel
 */
class ResourcesHelper(private val applicationContext: Context) {

    val cannotConnectToServer
        get() = applicationContext.getString(R.string.common_cannot_connect_to_server)

    val timeOutConnetion
        get() = applicationContext.getString(R.string.common_timeout_connect_to_server)

    val noInternetConnection
        get() = applicationContext.getString(R.string.common_no_internet_connection)

    val errorSystem
        get() = applicationContext.getString(R.string.common_error_system)
}