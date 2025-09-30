package ru.wolfram.client.data.tic_tac_toe

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import ru.wolfram.client.Logger
import ru.wolfram.client.data.common.KEY_KEY
import ru.wolfram.client.data.common.NAME_KEY
import ru.wolfram.client.data.mapper.toGameCreationResult
import ru.wolfram.client.data.mapper.toTicTacToeState
import ru.wolfram.client.data.mapper.toWhoDto
import ru.wolfram.client.data.mapper.toWhoResponse
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.data.network.tic_tac_toe.MessageDto
import ru.wolfram.client.data.network.tic_tac_toe.TicTacToeStateDto
import ru.wolfram.client.domain.common.GameCreationResult
import ru.wolfram.client.domain.tic_tac_toe.model.TicTacToeState
import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.model.WhoResponseState
import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository

class TicTacToeRepositoryImpl(
    private val apiService: ApiService,
    private val dataStore: DataStore<Preferences>,
    private val dispatcherIO: CoroutineDispatcher
) : TicTacToeRepository {
    private val nameKey = stringPreferencesKey(NAME_KEY)
    private val userKeyKey = stringPreferencesKey(KEY_KEY)
    private val move = MutableSharedFlow<Pair<Int, Int>?>(replay = 1)
    private val scope = CoroutineScope(SupervisorJob() + dispatcherIO)

    private var path: String? = null
    private lateinit var webSocket: DefaultClientWebSocketSession
    private lateinit var who: WhoResponseState.WhoResponse

    init {
        scope.launch {
            while (true) {
                Logger().log("SCOPE", scope.coroutineContext.job.children.toList().size.toString())
                delay(5000)
            }
        }
        Logger().log("ATTENX", "1 yes")
        scope.launch {
            while (!this@TicTacToeRepositoryImpl::who.isInitialized) {
                delay(100)
            }

            while (true) {
                ticTacToe.update {
                    webSocket.receiveDeserialized<TicTacToeStateDto>()
                        .toTicTacToeState()
                        .also { Logger().log("ATTENX", it.toString()) }
                }
            }
        }
        scope.launch {
            while (!this@TicTacToeRepositoryImpl::who.isInitialized) {
                delay(100)
            }
            Logger().log("ATTENX", "1 and 2 yes")
            move.collect {
                Logger().log("ATTENX", it.toString())
                it?.let {
                    Logger().log("ATTENX", it.toString())
                    webSocket.sendSerialized(
                        MessageDto.MoveDto(
                            it.first,
                            it.second,
                            who.key,
                            "Move"
                        )
                    )
                }
            }
        }
    }

    private val ticTacToe = MutableStateFlow(TicTacToeState())

    override fun getTicTacToe(desired: Who): StateFlow<TicTacToeState> {
        scope.launch {
            var varName: String? = null
            var varKey: String? = null
            dataStore.data.firstOrNull()?.let { prefs ->
                varName = prefs[nameKey]
                varKey = prefs[userKeyKey]
            }
            val name = varName
            val key = varKey
            if (name != null && key != null) {
                val result = apiService.randomTicTacToe(name, key).toGameCreationResult()
                if (result is GameCreationResult.GameKey) {
                    path = result.key
                }
            } else {
                throw RuntimeException("Unexpected behaviour when getting username or user key")
            }
            val pathVal = path
            if (pathVal != null) {
                webSocket = apiService.getTicTacToe(name, pathVal, desired.toWhoDto())
                who = apiService.handshake(name, desired.toWhoDto()).toWhoResponse()
            } else {
                throw RuntimeException("Unexpected behaviour when getting path to game session")
            }
        }
        return ticTacToe.asStateFlow()
    }

    override suspend fun move(x: Int, y: Int) {
        scope.launch {
            move.emit(Pair(x, y))
        }
    }

}