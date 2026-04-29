package com.lti.ltidesktop.data

import com.lti.ltidesktop.LineType
import com.lti.ltidesktop.OutputLine
import com.lti.ltidesktop.network.LtiApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TerminalRepository(private val apiService: LtiApiService) {

    private val _outputLines = MutableStateFlow<List<OutputLine>>(emptyList())
    val outputLines = _outputLines.asStateFlow()

    private val commandHistory = mutableListOf<String>()
    private var historyIndex = -1

    suspend fun executeCommand(command: String) {
        val cmd = command.trim()
        if (cmd.isEmpty()) return

        // Update history
        if (commandHistory.lastOrNull() != cmd) {
            commandHistory.add(cmd)
            if (commandHistory.size > 200) commandHistory.removeAt(0)
        }
        historyIndex = -1

        addLine("> $cmd", LineType.CMD)

        try {
            apiService.execute(cmd) { line ->
                addLine(line, classifyLine(line))
            }
        } catch (e: Exception) {
            addLine("[ERR] ${e.message}", LineType.ERROR)
        }
    }

    fun getHistoryUp(current: String): String {
        if (commandHistory.isEmpty()) return current
        if (historyIndex == -1) historyIndex = commandHistory.size
        if (historyIndex > 0) historyIndex--
        return commandHistory.getOrNull(historyIndex) ?: current
    }

    fun getHistoryDown(current: String): String {
        if (historyIndex == -1) return current
        historyIndex++
        return if (historyIndex >= commandHistory.size) {
            historyIndex = -1
            ""
        } else {
            commandHistory[historyIndex]
        }
    }

    fun clear() {
        _outputLines.value = emptyList()
    }

    fun addLine(text: String, type: LineType = LineType.NORMAL) {
        _outputLines.update { current ->
            val newList = current + OutputLine(text, type)
            if (newList.size > 5000) newList.drop(500) else newList
        }
    }

    private fun classifyLine(line: String): LineType = when {
        line.startsWith("[ERR]") -> LineType.ERROR
        line.startsWith("[SYS]") -> LineType.SYS
        line.startsWith("[WARN]") -> LineType.WARN
        else -> LineType.NORMAL
    }
}
