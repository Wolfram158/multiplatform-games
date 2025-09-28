package ru.wolfram.server.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.wolfram.server.model.Game
import ru.wolfram.server.model.GameCreationResult
import ru.wolfram.server.model.Label
import ru.wolfram.server.model.UserCreationResult
import ru.wolfram.server.service.Pending
import java.util.concurrent.ConcurrentHashMap
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@RestController
@RequestMapping(MainController.PREFIX)
class MainController {
    private val users = ConcurrentHashMap.newKeySet<String>()
    private val userToKey = ConcurrentHashMap<String, String>()
    private val pending = Pending()

    @PostMapping(ENTER)
    fun addUser(
        @RequestParam name: String,
        @RequestParam(required = false, defaultValue = "en") lang: String
    ): ResponseEntity<UserCreationResult> {
        if (users.add(name)) {
            val key = Uuid.random().toString()
            userToKey[name] = key
            return ResponseEntity.ok(UserCreationResult.UserKey(name, key))
        }
        return if (lang != "ru") {
            ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(UserCreationResult.Failure("User already exists!"))
        } else {
            ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(UserCreationResult.Failure("Пользователь с таким именем уже существует!"))
        }
    }

    @PostMapping(LEAVE)
    fun leaveUser(
        @RequestParam name: String,
        @RequestParam key: String
    ): ResponseEntity<String> {
        if (users.contains(name) && userToKey[name] == key) {
            userToKey.remove(name)
            users.remove(name)
            return ResponseEntity.ok("User has been removed successfully!")
        }
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body("Key is invalid or user does not exist!")
    }

    @GetMapping(NEW_TIC_TAC_TOE)
    fun newTicTacToe(
        @RequestParam name: String,
        @RequestParam key: String
    ): ResponseEntity<GameCreationResult> {
        if (!users.contains(name) || userToKey[name] != key) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(GameCreationResult.Failure("User does not exist or key is invalid!"))
        }
        return ResponseEntity.ok(GameCreationResult.GameKey(name, Uuid.random().toString()))
    }

    @GetMapping(RANDOM_TIC_TAC_TOE)
    fun randomTicTacToe(
        @RequestParam name: String,
        @RequestParam key: String
    ): ResponseEntity<GameCreationResult> {
        if (!users.contains(name) || userToKey[name] != key) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(GameCreationResult.Failure("User does not exist or key is invalid!"))
        }
        return ResponseEntity.ok(GameCreationResult.GameKey(name, pending.getKey(name)))
    }

    @GetMapping(GAMES)
    fun getGames(
        @RequestParam name: String,
        @RequestParam key: String,
        @RequestParam(required = false, defaultValue = "en") lang: String
    ): ResponseEntity<List<Game>> {
        if (!users.contains(name) || userToKey[name] != key) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(listOf())
        }
        return when (lang.lowercase()) {
            "ru" -> ResponseEntity.ok(
                listOf(
                    Game(
                        name = "Крестики-нулики",
                        randomUrl = "$BASE_URL$RANDOM_TIC_TAC_TOE",
                        createUrl = "$BASE_URL$NEW_TIC_TAC_TOE",
                        label = Label.TIC_TAC_TOE
                    ),
                )
            )

            else -> ResponseEntity.ok(
                listOf(
                    Game(
                        name = "Tic-tac-toe",
                        randomUrl = "$BASE_URL$RANDOM_TIC_TAC_TOE",
                        createUrl = "$BASE_URL$NEW_TIC_TAC_TOE",
                        label = Label.TIC_TAC_TOE
                    ),
                )
            )
        }
    }

    companion object {
        const val PREFIX = "/api/v1"
        const val BASE_URL = "http://localhost:8080$PREFIX"
        const val NEW_TIC_TAC_TOE = "/new-tic-tac-toe"
        const val RANDOM_TIC_TAC_TOE = "/random-tic-tac-toe"
        const val ENTER = "/enter"
        const val GAMES = "/games"
        const val LEAVE = "/leave"
    }
}