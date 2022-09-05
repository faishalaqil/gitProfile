package com.astro.test.aqil.features.dashboard.utils

import android.content.SharedPreferences
import com.astro.test.aqil.App
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.astro.test.aqil.features.dashboard.data.database.Profile

object SharedPrefManager {
    fun initializePrefs(): SharedPreferences {
        val masterKey = MasterKey.Builder(App.appContext!!, "MASTER_KEY_ALIAS")
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
//        val preferences = App.appContext!!.getSharedPreferences("shardprefs", Context.MODE_PRIVATE)
        val preferences = EncryptedSharedPreferences.create(
            App.appContext!!,
            "SHARED_PREF_PROFILE",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return preferences
    }

    var profile: Profile
        get() {
            val preferences = initializePrefs()
            val profile = Profile()
            profile.login = preferences.getString("KEY_LOGIN", null)
            return profile
        }
        set(profile) {
            val preferences = initializePrefs()
            val editor = preferences.edit()
            if (profile.login != null) {
                editor.putString("KEY_LOGIN", profile.login)
            }
            editor.apply()
        }

    fun clearVoucherPrefs() {
        val preferences = initializePrefs()
        val editor = preferences.edit()
        editor.remove("KEY_LOGIN")
        editor.apply()
    }
}