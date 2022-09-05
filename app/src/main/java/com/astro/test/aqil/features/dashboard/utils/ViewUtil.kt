package com.astro.test.aqil.features.dashboard.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Build.VERSION
import android.os.LocaleList
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.astro.R
import java.util.*

object ViewUtil {

    fun showOnBackPressedDialog(context: Context, fragment: Fragment, fragmentActivity: FragmentActivity){
        AlertDialog.Builder(context)
                .setMessage("Are you sure you want to go back home?")
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.common_yes)) { _, _ ->
                    val fm: FragmentManager = fragmentActivity.supportFragmentManager
                    for (i in 0 until fm.backStackEntryCount) {
                        fm.popBackStack()
                    }
                    NavHostFragment.findNavController(fragment)
                            .navigate(R.id.fragment_dashboard)
                }
                .setNegativeButton("No", null)
                .show()
    }

    fun applyLanguageContext(context: Context, locale: Locale?): Context {
        if (locale == null) return context
        if (locale == getLocale(context.resources.configuration)) return context

        return try {
            setupLocale(locale)
            val resources = context.resources
            val configuration = getOverridingConfig(locale, resources)
            updateResources(context, resources, configuration)
            context.createConfigurationContext(configuration)
        } catch (exception: Exception) {
            context
        }
    }

    private fun updateResources(
            context: Context,
            resources: Resources,
            config: Configuration
    ) {
        resources.updateConfiguration(config, resources.displayMetrics)
        if (context.applicationContext !== context) {
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }

    private fun setupLocale(locale: Locale) {
        Locale.setDefault(locale)

        if (VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.setDefault(LocaleList(locale))
        }
    }

    private fun getOverridingConfig(locale: Locale, resources: Resources): Configuration {
        val configuration = resources.configuration

        if (VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(LocaleList(locale))
        } else {
            configuration.locale = locale
        }
        return configuration
    }

    private fun getLocale(configuration: Configuration): Locale {
        return if (VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales.get(0)
        } else {
            configuration.locale
        }
    }


}