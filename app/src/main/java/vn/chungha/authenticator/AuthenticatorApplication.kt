package vn.chungha.authenticator

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import vn.chungha.authenticator.ui.setting.SettingMode
import javax.inject.Inject

@HiltAndroidApp
class AuthenticatorApplication() : Application() {

    @Inject
    lateinit var settings: SettingMode

    override fun onCreate() {
        super.onCreate()
        if (settings.darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        }
    }
}