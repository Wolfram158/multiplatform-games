package ru.wolfram.server.component

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import ru.wolfram.server.ServerApplication
import ru.wolfram.server.config.WebSocketConfig
import ru.wolfram.server.controller.MainController
import ru.wolfram.server.model.Cell
import ru.wolfram.server.model.GameCreationResult
import ru.wolfram.server.model.State
import ru.wolfram.server.model.UserCreationResult
import kotlin.concurrent.thread
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [ServerApplication::class])
class TicTacToeWebSocketHandlerTest {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate
    private lateinit var baseUrl: String

    @LocalServerPort
    private var serverPort = 0

    @BeforeEach
    fun init() {
        baseUrl = "http://localhost:$serverPort${MainController.PREFIX}"
    }

    @Test
    fun `GIVEN 2 users WHEN they play THEN null wins`() {
        val name1 = "Carl"
        val name2 = "Carrol"
        webSocketTest(name1, name2) { wsh1, session1, wsh2, session2 ->
            wsh1.move(session1, 0, 0)
            wsh2.move(session2, 0, 1)
            wsh1.move(session1, 1, 1)
            wsh2.move(session2, 1, 2)
            wsh1.move(session1, 2, 2)

            Thread.sleep(1000)

            assertEquals(wsh1.states, wsh2.states)
            assertEquals(
                listOf(
                    listOf(Cell.NULL, Cell.EMPTY, Cell.EMPTY),
                    listOf(Cell.CROSS, Cell.NULL, Cell.EMPTY),
                    listOf(Cell.EMPTY, Cell.CROSS, Cell.NULL)
                ),
                wsh1.states.last().cells
            )
            assertEquals(name1, wsh1.states.last().winner)
            assertEquals(name2, wsh1.states.last().loser)
        }
    }

    @Test
    fun `GIVEN 2 users WHEN they play THEN draw`() {
        val name1 = "Sam"
        val name2 = "Samuel"
        webSocketTest(name1, name2) { wsh1, session1, wsh2, session2 ->
            wsh1.move(session1, 0, 0)
            wsh2.move(session2, 0, 1)
            wsh1.move(session1, 1, 1)
            wsh2.move(session2, 1, 2)
            wsh1.move(session1, 0, 2)
            wsh2.move(session2, 2, 2)
            wsh1.move(session1, 1, 0)
            wsh2.move(session2, 2, 0)
            wsh1.move(session1, 2, 1)

            Thread.sleep(2000)

            assertEquals(wsh1.states, wsh2.states)
            assertEquals(
                listOf(
                    listOf(Cell.NULL, Cell.NULL, Cell.CROSS),
                    listOf(Cell.CROSS, Cell.NULL, Cell.NULL),
                    listOf(Cell.NULL, Cell.CROSS, Cell.CROSS),
                ),
                wsh1.states.last().cells
            )
            assertEquals(null, wsh1.states.last().winner)
            assertEquals(null, wsh1.states.last().loser)
            assertEquals(State.DRAW, wsh1.states.last().state)
        }
    }

    private fun webSocketTest(
        name1: String = "Carl",
        name2: String = "Carrol",
        t1: Long = 2000,
        t2: Long = 1000,
        callback: (ClientWebSocketHandler, WebSocketSession, ClientWebSocketHandler, WebSocketSession) -> Unit
    ) {
        val enter1 = "$baseUrl${MainController.ENTER}?name=$name1"
        val enter2 = "$baseUrl${MainController.ENTER}?name=$name2"

        val responseEnter1 = restTemplate.postForEntity(enter1, null, UserCreationResult.UserKey::class.java)
        val responseEnter2 = restTemplate.postForEntity(enter2, null, UserCreationResult.UserKey::class.java)

        val randomTicTacToe1 =
            "$baseUrl${MainController.RANDOM_TIC_TAC_TOE}?name=$name1&key=${responseEnter1.body?.key}"
        val randomTicTacToe2 =
            "$baseUrl${MainController.RANDOM_TIC_TAC_TOE}?name=$name2&key=${responseEnter2.body?.key}"

        lateinit var responseRandomGame1: GameCreationResult.GameKey
        lateinit var responseRandomGame2: GameCreationResult.GameKey

        val thread1 =
            thread {
                responseRandomGame1 = restTemplate.getForObject(
                    randomTicTacToe1,
                    GameCreationResult.GameKey::class.java
                )
            }

        Thread.sleep(t1)

        val thread2 =
            thread {
                responseRandomGame2 = restTemplate.getForObject(
                    randomTicTacToe2,
                    GameCreationResult.GameKey::class.java
                )
            }

        thread1.join()
        thread2.join()

        val wsh1 = ClientWebSocketHandler(name1)

        val session1 = StandardWebSocketClient().execute(
            wsh1,
            "ws://localhost:$serverPort${WebSocketConfig.TIC_TAC_TOE}/${responseRandomGame1.key}"
        ).join()

        val wsh2 = ClientWebSocketHandler(name2)

        val session2 = StandardWebSocketClient().execute(
            wsh2,
            "ws://localhost:$serverPort${WebSocketConfig.TIC_TAC_TOE}/${responseRandomGame2.key}"
        ).join()

        Thread.sleep(t2)

        callback(wsh1, session1, wsh2, session2)
    }
}