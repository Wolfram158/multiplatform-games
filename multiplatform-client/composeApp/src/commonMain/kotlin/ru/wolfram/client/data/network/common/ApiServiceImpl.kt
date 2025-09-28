package ru.wolfram.client.data.network.common

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.appendEncodedPathSegments
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.sync.Mutex
import ru.wolfram.client.Logger
import ru.wolfram.client.baseHttpUrl
import ru.wolfram.client.data.mapper.toWhoResponse
import ru.wolfram.client.data.network.auth.UserCreationResultDto
import ru.wolfram.client.data.network.games.GameDto
import ru.wolfram.client.data.network.tic_tac_toe.MessageDto
import ru.wolfram.client.data.network.tic_tac_toe.WhoDto
import ru.wolfram.client.data.network.tic_tac_toe.WhoResponseDto
import ru.wolfram.client.domain.tic_tac_toe.model.TicTacToeState
import ru.wolfram.client.domain.tic_tac_toe.model.WhoResponseState
import ru.wolfram.client.wsHost
import ru.wolfram.client.wsPort
import kotlin.concurrent.Volatile

class ApiServiceImpl(
    private val httpClient: HttpClient
) : ApiService {
    private val mtx = Mutex()
    override suspend fun getTicTacToe(
        name: String,
        path: String,
        desired: WhoDto,
        callback: suspend (WhoResponseState.WhoResponse, DefaultClientWebSocketSession) -> Unit
    ) {
        val webSocket = httpClient.webSocketSession(urlString = "ws://10.0.2.2:8080") {
            url {
                appendEncodedPathSegments(TIC_TAC_TOE, path)
            }
        }
        webSocket.sendSerialized(MessageDto.FirstMessageDto(name, desired, "FirstMessage"))
        val whoResponse = webSocket.receiveDeserialized<WhoResponseDto>()
        callback(whoResponse.toWhoResponse(), webSocket)
    }

    override fun getWhoResponse(): StateFlow<WhoResponseDto?> {
        return flow { emit(whoResponse) }.stateIn(
            CoroutineScope(Job()),
            SharingStarted.Lazily,
            null
        )
    }

    @Volatile
    private var whoResponse: WhoResponseDto? = null
    private val lastMove: ArrayDeque<Pair<Int, Int>> = ArrayDeque()

    private lateinit var webSocket: DefaultClientWebSocketSession

    @OptIn(InternalAPI::class)
    override suspend fun auth(name: String, lang: String): UserCreationResultDto {
        return httpClient.post(baseHttpUrl) {
            url {
                appendEncodedPathSegments(ENTER)
            }
            parameter(NAME_QUERY, name)
            parameter(LANG_QUERY, lang)
        }.body<UserCreationResultDto>()
    }

    override suspend fun getGames(name: String, key: String, lang: String): List<GameDto> {
        return httpClient.get(baseHttpUrl) {
            url {
                appendEncodedPathSegments(GAMES)
            }
            parameter(NAME_QUERY, name)
            parameter(KEY_QUERY, key)
            parameter(LANG_QUERY, lang)
        }.body<List<GameDto>>()
    }

    override suspend fun connect(path: String) {
        webSocket = httpClient.webSocketSession(host = wsHost, port = wsPort) {
            url {
                appendEncodedPathSegments(TIC_TAC_TOE, path)
            }
        }
    }

    override suspend fun randomTicTacToe(
        name: String,
        key: String
    ): GameCreationResultDto {
        return httpClient.get(baseHttpUrl) {
            url {
                appendEncodedPathSegments(RANDOM_TIC_TAC_TOE)
            }
            timeout {
                socketTimeoutMillis = 60_000L
            }
            parameter(NAME_QUERY, name)
            parameter(KEY_QUERY, key)
        }.body<GameCreationResultDto>()
    }

    override suspend fun handshake(name: String, desired: WhoDto): WhoResponseDto {
        delay(5000)
        webSocket.sendSerialized(MessageDto.FirstMessageDto(name, desired, "FirstMessage"))
        return webSocket.receiveDeserialized<WhoResponseDto>()
    }

    private val movesChannel = Channel<Pair<Int, Int>>(99)

    override suspend fun move(x: Int, y: Int) {
        Logger().log("MOVE_CALLED", "$x $y")
        movesChannel.send(Pair(x, y))
    }
//    override suspend fun move(x: Int, y: Int) {
//            Logger().log("LAST_MOVE", "$x $y")
//            mtx.lock()
//            lastMove.add(Pair(x, y))
//            mtx.unlock()
//    }

    companion object {
        private const val ENTER = "enter"
        private const val GAMES = "games"
        private const val RANDOM_TIC_TAC_TOE = "random-tic-tac-toe"
        private const val TIC_TAC_TOE = "tic-tac-toe"
        private const val NAME_QUERY = "name"
        private const val KEY_QUERY = "key"
        private const val LANG_QUERY = "lang"
    }
}
