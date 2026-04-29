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
                language = settingsRepository.getLanguage(),
                downloadPath = settingsRepository.getDownloadPath()
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
            is AppEvent.UpdateDownloadPath -> updateSettings { it.copy(downloadPath = event.path) }
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
        viewModelScope.launch {
            val downloadPath = _state.value.settings.downloadPath
            terminalRepository.addLine("[SYS] Listing files on host...", LineType.SYS)

            try {
                // 1. HTTP GET /files — get real file list from Android dump directory
                val remoteFiles = apiService.listRemoteFiles()

                if (remoteFiles.isEmpty()) {
                    terminalRepository.addLine(
                        "[WARN] No files in dump directory — run 'dump run' from Console first",
                        LineType.WARN
                    )
                    return@launch
                }

                terminalRepository.addLine(
                    "[SYS] Found ${remoteFiles.size} file(s): ${remoteFiles.joinToString { it.name }}",
                    LineType.SYS
                )

                // 2. Prepare local directory
                val dir = if (downloadPath.isNotBlank()) java.io.File(downloadPath) else null
                dir?.mkdirs()

                val newFiles = mutableListOf<DumpFile>()
                var totalBytes = 0L

                // 3. HTTP GET /files/{name} — download each file as raw ByteArray
                for (info in remoteFiles) {
                    terminalRepository.addLine(
                        "[SYS] Downloading ${info.name} (${info.size / 1024} KB)...",
                        LineType.SYS
                    )

                    val bytes = runCatching {
                        apiService.downloadFile(info.name)
                    }.getOrElse {
                        terminalRepository.addLine("[WARN] Failed to download ${info.name}: ${it.message}", LineType.WARN)
                        null
                    } ?: continue

                    totalBytes += bytes.size

                    // Save raw bytes to disk
                    val savedPath = if (dir != null) {
                        val outFile = java.io.File(dir, info.name)
                        runCatching { outFile.writeBytes(bytes) }.onFailure {
                            terminalRepository.addLine("[WARN] Could not save ${info.name}: ${it.message}", LineType.WARN)
                        }
                        outFile.absolutePath
                    } else {
                        "(in-memory)"
                    }

                    // For text types, decode for in-app viewing; for .so show a placeholder
                    val textContent = when (info.ext.lowercase()) {
                        "hpp", "txt", "json", "log" ->
                            runCatching { bytes.decodeToString() }.getOrDefault("(binary decode error)")
                        "so" ->
                            "// Binary .so file — ${bytes.size} bytes\n// Saved to: $savedPath\n// Use a hex editor or load into analysis tools."
                        else ->
                            "(binary — ${bytes.size} bytes)"
                    }

                    newFiles += DumpFile(
                        id      = "dl_${info.name}",
                        name    = info.name,
                        ext     = info.ext,
                        size    = "${bytes.size / 1024} KB",
                        time    = "just now",
                        path    = savedPath,
                        content = textContent
                    )
                }

                if (newFiles.isEmpty()) {
                    terminalRepository.addLine("[WARN] Download failed for all files", LineType.WARN)
                    return@launch
                }

                val savedMsg = dir?.absolutePath ?: "no path configured — set in Settings"
                terminalRepository.addLine(
                    "[OK] Downloaded ${newFiles.size} file(s), ${totalBytes / 1024} KB total → $savedMsg",
                    LineType.SYS
                )

                val newAction = ActionLog(
                    id    = "dl_${System.currentTimeMillis()}",
                    title = "Dump files downloaded",
                    meta  = "${newFiles.size} files · ${totalBytes / 1024} KB",
                    time  = "just now",
                    tone  = "success"
                )
                _state.update { s ->
                    s.copy(
                        files         = newFiles + s.files.filter { f -> newFiles.none { n -> n.id == f.id } },
                        recentActions = listOf(newAction) + s.recentActions
                    )
                }
                _effect.send(AppEffect.ScrollToBottom)

            } catch (e: Exception) {
                terminalRepository.addLine("[ERR] File sync failed: ${e.message}", LineType.ERROR)
            }
        }
    }

    private fun executeAction(key: String) {
        viewModelScope.launch {
            when (key) {
                "dump" -> {
                    // "Get Dump Files" → real sync via WebSocket
                    syncFiles()
                }
                "reconnect" -> {
                    // Real reconnect
                    disconnect()
                    kotlinx.coroutines.delay(500)
                    connect()
                }
                "diag" -> {
                    // Real diagnostics via WebSocket
                    terminalRepository.addLine("[SYS] Running diagnostics...", LineType.SYS)
                    runCatching {
                        apiService.execute("sys check") { line ->
                            terminalRepository.addLine(line, LineType.SYS)
                        }
                    }.onFailure {
                        terminalRepository.addLine("[ERR] Diagnostics failed: ${it.message}", LineType.ERROR)
                    }
                    _effect.send(AppEffect.ScrollToBottom)
                    val action = ActionLog(
                        id    = "diag_${System.currentTimeMillis()}",
                        title = "Diagnostics",
                        meta  = "sys check complete",
                        time  = "just now",
                        tone  = "success"
                    )
                    _state.update { it.copy(recentActions = listOf(action) + it.recentActions) }
                }
                "memdump" -> {
                    // Step 1: Run dump on server — writes files to /sdcard/UEDumper/ on Android
                    terminalRepository.addLine("[SYS] Starting memory dump on host...", LineType.SYS)
                    terminalRepository.addLine("[SYS] Files will be saved to: ${_state.value.settings.downloadPath.ifBlank { "configure in Settings" }}", LineType.SYS)
                    val dumpSuccess = runCatching {
                        // Stream live progress lines to console while dump runs
                        val lines = apiService.execute("dump run") { line ->
                            terminalRepository.addLine(line, LineType.NORMAL)
                        }
                        lines.any { it.contains("[OK]", ignoreCase = true) }
                    }.getOrElse {
                        terminalRepository.addLine("[ERR] dump run failed: ${it.message}", LineType.ERROR)
                        false
                    }
                    _effect.send(AppEffect.ScrollToBottom)

                    // Step 2: Download produced files from /sdcard/UEDumper/ to PC
                    if (dumpSuccess) {
                        terminalRepository.addLine("[SYS] Dump complete — downloading files from host...", LineType.SYS)
                        syncFiles()
                    } else {
                        terminalRepository.addLine("[WARN] Dump may have failed — check 'dump status' in Console", LineType.WARN)
                    }
                }
                "logs" -> {
                    // Sync logs via WebSocket
                    terminalRepository.addLine("[SYS] Fetching server logs...", LineType.SYS)
                    runCatching {
                        apiService.execute("log show") { line ->
                            terminalRepository.addLine(line, LineType.NORMAL)
                        }
                    }.onFailure {
                        terminalRepository.addLine("[ERR] Log sync failed: ${it.message}", LineType.ERROR)
                    }
                    _effect.send(AppEffect.ScrollToBottom)
                }
                else -> {
                    terminalRepository.addLine("[SYS] Action '$key' has no handler", LineType.WARN)
                }
            }
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
        settingsRepository.setDownloadPath(s.downloadPath)
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
