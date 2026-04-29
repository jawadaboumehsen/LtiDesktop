package com.lti.ltidesktop.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lti.ltidesktop.data.SettingsRepository

class ThemeManager(private val settingsRepository: SettingsRepository) {

    var isDark by mutableStateOf(settingsRepository.isDarkTheme())
        private set

    fun toggleTheme() {
        isDark = !isDark
        settingsRepository.setDarkTheme(isDark)
    }
}
