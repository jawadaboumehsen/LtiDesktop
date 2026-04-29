package com.lti.ltidesktop.presentation

import com.lti.ltidesktop.OutputLine
import com.lti.ltidesktop.network.ConnectionState

enum class Screen {
    HOME,
    CLI,
    SETTINGS
}

data class SettingsState(
    val autoConnect: Boolean = false,
    val historyLimit: Int = 1000,
    val connectionTimeout: Int = 5000,
    val fontSize: Int = 13,
    val opacity: Float = 1.0f,
    val language: String = "English"
)

data class AppState(
    val currentScreen: Screen = Screen.HOME,
    val isSidebarExpanded: Boolean = true,
    val host: String = "",
    val port: String = "",
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val outputLines: List<OutputLine> = emptyList(),
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val settings: SettingsState = SettingsState()
)
