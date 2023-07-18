package com.example.filmsapp.fragments.setting

import android.content.Context
import android.provider.Settings.System.getString
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.example.filmsapp.R
import com.example.filmsapp.dataPrefs.IPrefs
import com.example.filmsapp.dataPrefs.Prefs
import kotlinx.coroutines.flow.MutableStateFlow

class SettingVM(private val prefs: IPrefs) : ViewModel() {

    val systemThemeString = MutableStateFlow("")

    fun initialize(context: Context) {
        when (prefs.modeTheme) {
            1 -> systemThemeString.value = context.getString(R.string.light)
            2 -> systemThemeString.value = context.getString(R.string.night)
            3 -> systemThemeString.value = context.getString(R.string.system)
        }
    }

    fun themeSetListener(context: Context, themeCode: Int) {
        val themes = mapOf(
            1 to Triple(AppCompatDelegate.MODE_NIGHT_NO, R.string.light, 1),
            2 to Triple(AppCompatDelegate.MODE_NIGHT_YES, R.string.night, 2),
            3 to Triple(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, R.string.system, 3)
        )

        themes[themeCode]?.let { (mode, stringRes, modeTheme) ->
            AppCompatDelegate.setDefaultNightMode(mode)
            systemThemeString.value = context.getString(stringRes)
            prefs.modeTheme = modeTheme
        }
    }
}