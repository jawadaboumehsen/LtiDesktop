package com.lti.ltidesktop.presentation

sealed interface AppEvent {
    data class NavigateTo(val screen: Screen) : AppEvent
    object ToggleSidebar : AppEvent
    data class UpdateHost(val host: String) : AppEvent
    data class UpdatePort(val port: String) : AppEvent
    data class UpdateUser(val user: String) : AppEvent
    object Connect : AppEvent
    object Disconnect : AppEvent
    data class SendCommand(val input: String) : AppEvent
    object ClearOutput : AppEvent
    data class HistoryUp(val currentInput: String) : AppEvent
    data class HistoryDown(val currentInput: String) : AppEvent
    
    // Settings Events
    data class UpdateAutoConnect(val enabled: Boolean) : AppEvent
    data class UpdateHistoryLimit(val limit: Int) : AppEvent
    data class UpdateConnectionTimeout(val timeout: Int) : AppEvent
    data class UpdateFontSize(val size: Int) : AppEvent
    data class UpdateOpacity(val opacity: Float) : AppEvent
    data class UpdateLanguage(val language: String) : AppEvent
    object SaveSettings : AppEvent

    // File & Action Hub Events
    data class SelectFile(val id: String?) : AppEvent
    object SyncFiles : AppEvent
    data class DeleteFile(val id: String) : AppEvent
    data class ExecuteAction(val actionKey: String) : AppEvent
}
