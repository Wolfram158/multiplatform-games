package ru.wolfram.server.component

import com.google.gson.Gson
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import ru.wolfram.server.exception.UnexpectedException
import ru.wolfram.server.model.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Component
class TicTacToeWebSocketHandler : TextWebSocketHandler() {
    private val gson = Gson()
    private val kotlinxJson = Json {
        ignoreUnknownKeys = true
    }
    private val pathToUsersCount = HashMap<String, Int>()
    private val pathToSessions = HashMap<String, Set<WebSocketSession>>()
    private val pathToGameState = HashMap<String, TicTacToeState>()
    private val pathToLock = ConcurrentHashMap<String, ReentrantLock>()
    private val pathToCrossName = HashMap<String, String>()
    private val pathToNullName = HashMap<String, String>()
    private val pathToCrossKey = HashMap<String, String>()
    private val pathToNullKey = HashMap<String, String>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val path = session.getPathOrThrow()
        withLock(path) {
            val usersCount = pathToUsersCount.getOrDefault(path, 0)
            if (usersCount == 2) {
                session.close()
                return@withLock
            }
            val new = pathToSessions[path]?.toMutableSet() ?: mutableSetOf()
            new.add(session)
            pathToSessions[path] = new
            pathToGameState[path] = TicTacToeState()
            pathToUsersCount[path] = usersCount + 1
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val path = session.getPathOrThrow()
        withLock(path) {
            when (val msg = kotlinxJson.decodeFromString<Message>(message.payload)) {
                is Message.FirstMessage -> {
                    val who = when (msg.desired) {
                        Who.CROSS -> {
                            if (pathToCrossName[path] == null) {
                                pathToCrossName[path] = msg.name
                                Who.CROSS
                            } else {
                                pathToNullName[path] = msg.name
                                Who.NULL
                            }
                        }

                        Who.NULL -> {
                            if (pathToNullName[path] == null) {
                                pathToNullName[path] = msg.name
                                Who.NULL
                            } else {
                                pathToCrossName[path] = msg.name
                                Who.CROSS
                            }
                        }
                    }
                    val key = Uuid.random().toString()
                    when (who) {
                        Who.CROSS -> {
                            pathToCrossKey[path] = key
                        }

                        Who.NULL -> {
                            pathToNullKey[path] = key
                        }
                    }
                    val json = gson.toJson(WhoResponse(who, key), WhoResponse::class.java)
                    session.sendMessage(TextMessage(json))
                }

                is Message.Move -> {
                    val state = pathToGameState[path] ?: throw UnexpectedException("Unknown path $path")
                    val nextState = getNextState(state, path, msg) ?: return@withLock
                    pathToGameState[path] = nextState
                    val json = gson.toJson(nextState, TicTacToeState::class.java)
                    broadcast(path, TextMessage(json))
                    if (nextState.state != State.PROGRESS) {
                        freePath(path)
                    }
                }
            }
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val path = session.getPathOrThrow()
        withLock(path) {
            pathToUsersCount[path] = (pathToUsersCount[path] ?: throw UnexpectedException("How is it possible?")) - 1
            val sessions = pathToSessions[path]?.toMutableSet() ?: mutableSetOf()
            sessions.remove(session)
            if (sessions.isEmpty()) {
                freePath(path)
            } else {
                pathToSessions[path] = sessions
            }
        }
    }

    private fun freePath(path: String) {
        pathToSessions[path]?.forEach { it.close() }
        pathToSessions.remove(path)
        pathToGameState.remove(path)
        pathToUsersCount.remove(path)
        pathToLock.remove(path)
        pathToCrossName.remove(path)
        pathToNullName.remove(path)
        pathToCrossKey.remove(path)
        pathToNullKey.remove(path)
    }

    private fun withLock(path: String, action: () -> Unit) {
        val lock = pathToLock.getOrPut(path) { ReentrantLock() }
        lock.lock()
        action()
        lock.unlock()
    }

    private fun getNextState(state: TicTacToeState, path: String, move: Message.Move): TicTacToeState? {
        if (state.state != State.PROGRESS ||
            move.x !in 0..2 ||
            move.y !in 0..2 ||
            state.cells[move.y][move.x] != Cell.EMPTY ||
            !isKeyValid(path, state.whoseMove, move.key)
        ) {
            return null // throw RulesViolationException()
        }
        val cell = state.whoseMove.toCell()
        val cells = state.cells.map { it.toMutableList() }
        cells[move.y][move.x] = cell
        return when (listOf(
            checkDiagonals(cells, cell),
            checkHorizontals(cells, cell),
            checkVerticals(cells, cell)
        ).reduce()) {
            State.PROGRESS -> {
                state.copy(whoseMove = state.whoseMove.next(), cells = cells)
            }

            State.WIN_FAILURE -> {
                state.copy(
                    state = State.WIN_FAILURE,
                    winner = state.whoseMove.getNameByCell(path),
                    loser = state.whoseMove.next().getNameByCell(path),
                    whoseMove = state.whoseMove.next(),
                    cells = cells
                )
            }

            State.DRAW -> {
                state.copy(state = State.DRAW, whoseMove = state.whoseMove.next(), cells = cells)
            }
        }
    }

    private fun isKeyValid(path: String, who: Who, key: String): Boolean {
        return when (who) {
            Who.CROSS -> {
                pathToCrossKey[path] == key
            }

            Who.NULL -> {
                pathToNullKey[path] == key
            }
        }
    }

    private fun Who.getNameByCell(path: String) = when (this) {
        Who.CROSS -> pathToCrossName[path]
        Who.NULL -> pathToNullName[path]
    }

    private fun broadcast(path: String, message: TextMessage) {
        try {
            pathToSessions[path]?.forEach {
                it.sendMessage(message)
            }
        } catch (e: Exception) {
            println("Error sending message: ${e.message}")
        }
    }

    companion object {
        private fun Who.next() = when (this) {
            Who.CROSS -> Who.NULL
            Who.NULL -> Who.CROSS
        }

        private fun Who.toCell() = when (this) {
            Who.CROSS -> Cell.CROSS
            Who.NULL -> Cell.NULL
        }

        private fun checkDiagonals(cells: List<List<Cell>>, cell: Cell): State {
            if (cells[0][0] == cell && cells[1][1] == cell && cells[2][2] == cell) {
                return State.WIN_FAILURE
            }
            if (cells[0][2] == cell && cells[1][1] == cell && cells[2][0] == cell) {
                return State.WIN_FAILURE
            }
            return if (!cells.flatten()
                    .contains(Cell.EMPTY)
            ) {
                State.DRAW
            } else {
                State.PROGRESS
            }
        }

        private fun checkHorizontals(cells: List<List<Cell>>, cell: Cell): State {
            for (row in cells) {
                if (row.all { it == cell }) {
                    return State.WIN_FAILURE
                }
            }
            return if (!cells.flatten()
                    .contains(Cell.EMPTY)
            ) {
                State.DRAW
            } else {
                State.PROGRESS
            }
        }

        private fun checkVerticals(cells: List<List<Cell>>, cell: Cell): State {
            if (cells[0][0] == cell && cells[1][0] == cell && cells[2][0] == cell) {
                return State.WIN_FAILURE
            }
            if (cells[0][1] == cell && cells[1][1] == cell && cells[2][1] == cell) {
                return State.WIN_FAILURE
            }
            if (cells[0][2] == cell && cells[1][2] == cell && cells[2][2] == cell) {
                return State.WIN_FAILURE
            }
            return if (!cells.flatten()
                    .contains(Cell.EMPTY)
            ) {
                State.DRAW
            } else {
                State.PROGRESS
            }
        }

        private fun List<State>.reduce(): State {
            forEach {
                if (it == State.WIN_FAILURE) {
                    return State.WIN_FAILURE
                }
                if (it == State.DRAW) {
                    return State.DRAW
                }
            }
            return State.PROGRESS
        }
    }
}