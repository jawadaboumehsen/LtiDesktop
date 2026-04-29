package com.lti.ltidesktop.network

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random

class LtiApiService(private val client: HttpClient) {

    private val _state = MutableStateFlow(ConnectionState.DISCONNECTED)
    val state: StateFlow<ConnectionState> = _state

    private var session: DefaultClientWebSocketSession? = null
    private var receiveJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val handlers = mutableMapOf<String, (CommandResponse) -> Unit>()
    private val handlersMutex = Mutex()

    suspend fun connect(host: String, port: Int): List<String> {
        _state.value = ConnectionState.CONNECTING
        return try {
            session = client.webSocketSession("ws://$host:$port/cli")
            _state.value = ConnectionState.CONNECTED
            startReceiveLoop()
            
            val welcome = CompletableDeferred<List<String>>()
            handlersMutex.withLock {
                handlers["_welcome"] = { resp -> if (resp.done) welcome.complete(resp.lines) }
            }
            
            withTimeoutOrNull(5000L) { welcome.await() } ?: listOf("[SYS] Connected (no welcome)")
        } catch (e: Exception) {
            _state.value = ConnectionState.DISCONNECTED
            throw e
        }
    }

    suspend fun execute(
        command: String,
        onPartialLine: ((String) -> Unit)? = null
    ): List<String> {
        val sess = session ?: error("Not connected")
        val id = Random.nextLong().toString()
        val accumulated = mutableListOf<String>()
        val done = CompletableDeferred<List<String>>()

        handlersMutex.withLock {
            handlers[id] = { resp ->
                resp.lines.forEach { line ->
                    accumulated += line
                    onPartialLine?.invoke(line)
                }
                if (resp.done) {
                    scope.launch {
                        handlersMutex.withLock {
                            handlers.remove(id)
                        }
                    }
                    done.complete(accumulated.toList())
                }
            }
        }

        sess.send(Frame.Text(Json.encodeToString(CommandRequest(id, command))))
        return done.await()
    }

    fun disconnect() {
        receiveJob?.cancel()
        session?.cancel()
        session = null
        scope.launch {
            handlersMutex.withLock {
                handlers.clear()
            }
        }
        _state.value = ConnectionState.DISCONNECTED
    }

    private fun startReceiveLoop() {
        receiveJob = scope.launch {
            try {
                val sess = session!!
                for (frame in sess.incoming) {
                    if (frame !is Frame.Text) continue
                    runCatching {
                        val resp = Json.decodeFromString<CommandResponse>(frame.readText())
                        handlersMutex.withLock {
                            handlers[resp.id]?.invoke(resp)
                        }
                    }
                }
            } catch (_: Exception) {
            } finally {
                _state.value = ConnectionState.DISCONNECTED
                handlersMutex.withLock {
                    handlers.clear()
                }
            }
        }
    }
}
