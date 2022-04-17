package vn.chungha.authenticator.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharePreference @Inject constructor(
    val gson: Gson
) {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val KEY_ID_TOKEN = "KEY_ID_TOKEN"
        const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
        const val KEY_USER = "KEY_USER"
        const val KEY_CHILDREN = "KEY_CHILDREN"
    }

    inline fun <reified T> save(key: String, any: T) {
        val editor = sharedPreferences.edit()
        when (any) {
            is String -> editor.putString(key, any)
            is Float -> editor.putFloat(key, any)
            is Int -> editor.putInt(key, any)
            is Long -> editor.putLong(key, any)
            is Boolean -> editor.putBoolean(key, any)
            else -> editor.putString(key, gson.toJson(any))
        }
        editor.apply()
    }

    inline fun <reified T> get(key: String): T? {
        when (T::class) {
            Float::class -> return sharedPreferences.getFloat(key, 0f) as T
            Int::class -> return sharedPreferences.getInt(key, 0) as T
            Long::class -> return sharedPreferences.getLong(key, 0) as T
            String::class -> return sharedPreferences.getString(key, "") as T
            Boolean::class -> return sharedPreferences.getBoolean(key, false) as T
            else -> {
                val any = sharedPreferences.getString(key, "")
                val type = object : TypeToken<T>() {}.type
                if (any != null && any.isNotEmpty()) {
                    return gson.fromJson<T>(any, type)
                }
            }
        }
        return null
    }
}
