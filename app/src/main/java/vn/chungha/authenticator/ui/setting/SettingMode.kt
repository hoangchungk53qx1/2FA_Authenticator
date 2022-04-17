package vn.chungha.authenticator.ui.setting

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

private const val DARK_MODE_KEY = "darkMode"

@Singleton
class SettingMode @Inject constructor(val sharedPreferences: SharedPreferences) {
    var darkMode: Boolean
        get() = sharedPreferences.getBoolean(DARK_MODE_KEY, false)
        set(value) = sharedPreferences.edit().putBoolean(DARK_MODE_KEY, value).apply()
}