package com.example.filmsapp.dataPrefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Prefs(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("FILMS_APP", Context.MODE_PRIVATE)


    companion object {
        private const val KEY_MODE_THEME = "modeTheme"

        fun getInstance(context: Context): Prefs {
            return Prefs(context)
        }
    }

    var modeTheme: Int?
        get() = sharedPreferences.getInt(KEY_MODE_THEME, 0)
        set(value) = sharedPreferences.edit { putInt(KEY_MODE_THEME, value!!) }


    // Метод для удаления всех преференсов
    fun clearPrefs() {
        sharedPreferences.edit().clear().apply()
    }
}
