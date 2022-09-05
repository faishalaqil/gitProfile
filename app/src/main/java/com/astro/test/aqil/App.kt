package com.astro.test.aqil

import android.app.Activity
import android.app.Application
import android.content.*
import com.astro.test.aqil.features.dashboard.di.appModule
import com.astro.test.aqil.features.dashboard.di.repoModule
import com.astro.test.aqil.features.dashboard.di.viewModelModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    private val prefs by inject<SharedPreferences>()

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        //Stetho.initializeWithDefaults(this)
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
    }

    companion object {
        var appContext: Context? = null
        var mCurrentActivity: Activity? = null

        fun getCurrentActivity(): Activity? {
            return mCurrentActivity
        }

        fun setCurrentActivity(mCurrentActivity1: Activity?) {
            mCurrentActivity = mCurrentActivity1
        }
    }
}