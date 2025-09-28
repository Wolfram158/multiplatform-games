package ru.wolfram.server.component

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.web.socket.*
import ru.wolfram.server.model.Message
import ru.wolfram.server.model.TicTacToeState
import ru.wolfram.server.model.Who
import ru.wolfram.server.model.WhoResponse

class ClientWebSocketHandler(
    private val name: String
) : WebSocketHandler {
    private val json = Json {
        ignoreUnknownKeys = true
    }
    private lateinit var playKey: String
    private var waitForFirstMessage = true
    private val _states = mutableListOf<TicTacToeState>()
    val states
        get() = _states

    override fun afterConnectionEstablished(session: WebSocketSession) {
        session.sendMessage(
            TextMessage(
                json.encodeToString(
                    Message.FirstMessage(
                        name,
                        Who.NULL,
                        "FirstMessage"
                    )
                )
            )
        )
    }

    override fun handleMessage(
        session: WebSocketSession,
        message: WebSocketMessage<*>
    ) {
        if (waitForFirstMessage) {
            waitForFirstMessage = false
            val whoResponse = json.decodeFromString<WhoResponse>(message.payload as String)
            playKey = whoResponse.key
        } else {
            _states.add(json.decodeFromString<TicTacToeState>(message.payload as String))
        }
    }

    override fun handleTransportError(
        session: WebSocketSession,
        exception: Throwable
    ) {
    }

    override fun afterConnectionClosed(
        session: WebSocketSession,
        closeStatus: CloseStatus
    ) {
    }

    override fun supportsPartialMessages(): Boolean {
        return false
    }

    fun move(session: WebSocketSession, x: Int, y: Int) {
        session.sendMessage(TextMessage(json.encodeToString(Message.Move(x, y, playKey, "Move"))))
    }

}