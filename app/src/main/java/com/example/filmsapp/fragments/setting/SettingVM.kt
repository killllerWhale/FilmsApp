package com.example.filmsapp.fragments.setting

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.example.filmsapp.dataPrefs.Prefs
import kotlinx.coroutines.flow.MutableStateFlow

class SettingVM : ViewModel() {

    val systemThemeSwitch = MutableStateFlow(false)
    val themeSwitch = MutableStateFlow(false)
    private lateinit var prefs: Prefs

    fun initialize(context: Context) {
        prefs = Prefs.getInstance(context)
        when (prefs.modeTheme) {
            1 -> {
                themeSwitch.value = true
                systemThemeSwitch.value = false
            }

            0 -> {
                themeSwitch.value = false
                systemThemeSwitch.value = false
            }

            else -> systemThemeSwitch.value = true
        }
    }

    fun themeSwitchSetListener(isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            prefs.modeTheme = 1
            systemThemeSwitch.value = false
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            prefs.modeTheme = 0
        }
    }

    fun systemThemeSwitchSetListener(isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            prefs.modeTheme = -1
            themeSwitch.value = false
        } else {
            if (themeSwitch.value) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                prefs.modeTheme = 1
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                prefs.modeTheme = 0
            }
        }
    }
}