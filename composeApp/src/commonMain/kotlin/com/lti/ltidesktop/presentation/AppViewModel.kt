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
        }
    }

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
