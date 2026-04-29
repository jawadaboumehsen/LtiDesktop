package com.lti.ltidesktop.presentation

import com.lti.ltidesktop.OutputLine
import com.lti.ltidesktop.network.ConnectionState

enum class Screen {
    HOME,
    DASHBOARD,
    FILES,
    CONSOLE,
    SETTINGS
}

data class DumpFile(
    val id: String,
    val name: String,
    val ext: String,
    val size: String,
    val time: String,
    val path: String,
    val content: String
)

data class ActionLog(
    val id: String,
    val title: String,
    val meta: String,
    val time: String,
    val tone: String // success, info, warn, error
)

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
    val host: String = "192.168.1.42",
    val port: String = "22",
    val user: String = "root",
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val outputLines: List<OutputLine> = emptyList(),
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val settings: SettingsState = SettingsState(),

    // Design System Screen States
    val files: List<DumpFile> = emptyList(),
    val selectedFileId: String? = null,
    val recentActions: List<ActionLog> = emptyList()
)
