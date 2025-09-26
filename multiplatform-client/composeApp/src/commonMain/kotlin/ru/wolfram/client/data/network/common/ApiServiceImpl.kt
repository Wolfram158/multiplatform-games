package ru.wolfram.client.data.network.common

import androidx.compose.ui.platform.LocalGraphicsContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.appendEncodedPathSegments
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import ru.wolfram.client.baseHttpUrl
import ru.wolfram.client.data.network.auth.UserCreationResultDto
import ru.wolfram.client.data.network.games.GameDto
import ru.wolfram.client.data.network.tic_tac_toe.MessageDto
import ru.wolfram.client.data.network.tic_tac_toe.TicTacToeStateDto
import ru.wolfram.client.data.network.tic_tac_toe.WhoDto
import ru.wolfram.client.data.network.tic_tac_toe.WhoResponseDto
import ru.wolfram.client.wsHost
import ru.wolfram.client.wsPort

class ApiServiceImpl(
    private val httpClient: HttpClient
) : ApiService {

    override val ticTacToe: Flow<TicTacToeStateDto> = flow {
        delay(5000)
        while (true) {
            emit(webSocket.receiveDeserialized<TicTacToeStateDto>())
        }
    }

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
        webSocket = httpClient.webSocketSession(host = wsHost, port = wsPort, path = path)
    }

    override suspend fun randomTicTacToe(
        name: String,
        key: String
    ): GameCreationResultDto {
        return httpClient.get(baseHttpUrl) {
            url {
                appendEncodedPathSegments(RANDOM_TIC_TAC_TOE)
            }
            parameter(NAME_QUERY, name)
            parameter(KEY_QUERY, key)
        }.body<GameCreationResultDto>()
    }

    override suspend fun handshake(name: String, desired: WhoDto): WhoResponseDto {
        webSocket.sendSerialized(MessageDto.FirstMessageDto(name, desired))
        return webSocket.receiveDeserialized<WhoResponseDto>()
    }

    override suspend fun move(x: Int, y: Int, key: String) {
        webSocket.sendSerialized(MessageDto.MoveDto(x, y, key))
    }

    companion object {
        private const val ENTER = "enter"
        private const val GAMES = "games"
        private const val RANDOM_TIC_TAC_TOE = "random-tic-tac-toe"
        private const val NAME_QUERY = "name"
        private const val KEY_QUERY = "key"
        private const val LANG_QUERY = "lang"
    }
}