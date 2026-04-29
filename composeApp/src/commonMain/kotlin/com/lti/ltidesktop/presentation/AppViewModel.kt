package com.lti.ltidesktop.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lti.ltidesktop.data.SettingsRepository
import com.lti.ltidesktop.data.TerminalRepository
import com.lti.ltidesktop.network.ConnectionState
import com.lti.ltidesktop.network.LtiApiService
import com.lti.ltidesktop.LineType
import com.lti.ltidesktop.OutputLine
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(
    private val apiService: LtiApiService,
    private val terminalRepository: TerminalRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        AppState(
            host = settingsRepository.getHost(),
            port = settingsRepository.getPort(),
            settings = SettingsState(
                autoConnect = settingsRepository.getAutoConnect(),
                historyLimit = settingsRepository.getHistoryLimit(),
                connectionTimeout = settingsRepository.getTimeout(),
                fontSize = settingsRepository.getFontSize(),
                opacity = settingsRepository.getOpacity(),
                language = settingsRepository.getLanguage()
            )
        )
    )
    val state: StateFlow<AppState> = _state.asStateFlow()

    private val _effect = Channel<AppEffect>(Channel.BUFFERED)
    val effect: Flow<AppEffect> = _effect.receiveAsFlow()

    init {
        // Collect state from repositories
        combine(
            apiService.state,
            terminalRepository.outputLines
        ) { connState, lines ->
            _state.update { it.copy(connectionState = connState, outputLines = lines) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: AppEvent) {
        when (event) {
            is AppEvent.NavigateTo -> {
                _state.update { it.copy(currentScreen = event.screen) }
            }
            AppEvent.ToggleSidebar -> {
                _state.update { it.copy(isSidebarExpanded = !it.isSidebarExpanded) }
            }
            is AppEvent.UpdateHost -> updateHost(event.host)
            is AppEvent.UpdatePort -> updatePort(event.port)
            is AppEvent.UpdateUser -> _state.update { it.copy(user = event.user) }
            AppEvent.Connect -> connect()
            AppEvent.Disconnect -> disconnect()
            is AppEvent.SendCommand -> sendCommand(event.input)
            AppEvent.ClearOutput -> clearOutput()
            is AppEvent.HistoryUp -> historyUp(event.currentInput)
            is AppEvent.HistoryDown -> historyDown(event.currentInput)
            
            // Settings Events
            is AppEvent.UpdateAutoConnect -> updateSettings { it.copy(autoConnect = event.enabled) }
            is AppEvent.UpdateHistoryLimit -> updateSettings { it.copy(historyLimit = event.limit) }
            is AppEvent.UpdateConnectionTimeout -> updateSettings { it.copy(connectionTimeout = event.timeout) }
            is AppEvent.UpdateFontSize -> updateSettings { it.copy(fontSize = event.size) }
            is AppEvent.UpdateOpacity -> updateSettings { it.copy(opacity = event.opacity) }
            is AppEvent.UpdateLanguage -> updateSettings { it.copy(language = event.language) }
            AppEvent.SaveSettings -> saveSettings()

            // File & Action Hub Events
            is AppEvent.SelectFile -> {
                _state.update { it.copy(selectedFileId = event.id) }
            }
            AppEvent.SyncFiles -> syncFiles()
            is AppEvent.DeleteFile -> {
                _state.update { s -> s.copy(files = s.files.filter { it.id != event.id }, selectedFileId = if (s.selectedFileId == event.id) null else s.selectedFileId) }
            }
            is AppEvent.ExecuteAction -> executeAction(event.actionKey)
        }
    }

    private fun syncFiles() {
        val stamp = (1000..9999).random()
        val newFile = DumpFile(
            id = "sync_$stamp",
            name = "sync_$stamp.txt",
            ext = "txt",
            size = "${(10..90).random()} KB",
            time = "just now",
            path = "/var/log/sync_$stamp.txt",
            content = "[INFO] sync started\n[OK]   12 files transferred\n[OK]   sync complete · $stamp\n"
        )
        _state.update { it.copy(files = listOf(newFile) + it.files) }

        val newAction = ActionLog(
            id = "action_${newFile.id}",
            title = "Logs synced",
            meta = "12 files · ${newFile.size}",
            time = "just now",
            tone = "info"
        )
        _state.update { it.copy(recentActions = listOf(newAction) + it.recentActions) }
    }

    private fun executeAction(key: String) {
        viewModelScope.launch {
            // Simulate processing delay
            val stamp = (1000..9999).random()

            val (title, meta, tone, ext) = when(key) {
                "dump" -> Quadruple("Dump pulled", "system.hpp · 2.4 MB", "success", "hpp")
                "memdump" -> Quadruple("Memory captured", "mem_snapshot_$stamp.hpp", "success", "hpp")
                "diag" -> Quadruple("Diagnostics passed", "all checks ok", "success", "txt")
                "reboot" -> Quadruple("Host Rebooted", "cold restart complete", "warn", "txt")
                else -> Quadruple("Action executed", "completed successfully", "info", "txt")
            }

            val newFile = DumpFile(
                id = "f_$stamp",
                name = if (ext == "hpp") "system_dump_$stamp.hpp" else "trace_$stamp.txt",
                ext = ext,
                size = "${(1..4).random()}.${(0..9).random()} MB",
                time = "just now",
                path = "/var/dumps/${if (ext == "hpp") "system_dump" else "trace"}_$stamp.$ext",
                content = "// Captured ${key.uppercase()}\n// ID: $stamp\n#include <cstdint>\n\nnamespace lti::dump {\n  // capture logic for $key\n}\n"
            )

            val newAction = ActionLog(
                id = "log_$stamp",
                title = title,
                meta = meta,
                time = "just now",
                tone = tone
            )

            _state.update { it.copy(
                files = listOf(newFile) + it.files,
                recentActions = listOf(newAction) + it.recentActions
            ) }
        }
    }

    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

    private fun updateSettings(update: (SettingsState) -> SettingsState) {
        _state.update { it.copy(settings = update(it.settings)) }
    }

    private fun saveSettings() {
        val s = _state.value.settings
        settingsRepository.setAutoConnect(s.autoConnect)
        settingsRepository.setHistoryLimit(s.historyLimit)
        settingsRepository.setTimeout(s.connectionTimeout)
        settingsRepository.setFontSize(s.fontSize)
        settingsRepository.setOpacity(s.opacity)
        settingsRepository.setLanguage(s.language)
        // Optionally show a toast or feedback
    }

    private fun updateHost(newHost: String) {
        _state.update { it.copy(host = newHost) }
        settingsRepository.setHost(newHost)
    }

    private fun updatePort(newPort: String) {
        _state.update { it.copy(port = newPort) }
        settingsRepository.setPort(newPort)
    }

    private fun connect() {
        val currentState = _state.value
        viewModelScope.launch {
            terminalRepository.addLine("[SYS] Connecting to ${currentState.host}:${currentState.port}...", LineType.SYS)
            try {
                val portInt = currentState.port.toIntOrNull() ?: run {
                    terminalRepository.addLine("[ERR] Invalid port: ${currentState.port}", LineType.ERROR)
                    return@launch
                }
                val welcome = apiService.connect(currentState.host, portInt)
                welcome.forEach { terminalRepository.addLine(it, LineType.SYS) }
                _effect.send(AppEffect.ScrollToBottom)
            } catch (e: Exception) {
                terminalRepository.addLine("[ERR] Connection failed: ${e.message}", LineType.ERROR)
            }
        }
    }

    private fun disconnect() {
        apiService.disconnect()
        terminalRepository.addLine("[SYS] Disconnected.", LineType.SYS)
    }

    private fun sendCommand(input: String) {
        viewModelScope.launch {
            terminalRepository.executeCommand(input)
            _effect.send(AppEffect.ScrollToBottom)
        }
    }

    private fun historyUp(current: String) {
        val result = terminalRepository.getHistoryUp(current)
        viewModelScope.launch { _effect.send(AppEffect.UpdateInput(result)) }
    }

    private fun historyDown(current: String) {
        val result = terminalRepository.getHistoryDown(current)
        viewModelScope.launch { _effect.send(AppEffect.UpdateInput(result)) }
    }

    private fun clearOutput() = terminalRepository.clear()

    override fun onCleared() {
        super.onCleared()
        apiService.disconnect()
    }
}
