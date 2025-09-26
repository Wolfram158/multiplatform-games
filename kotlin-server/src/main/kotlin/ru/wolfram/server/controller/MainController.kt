package ru.wolfram.server.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.wolfram.server.model.Game
import ru.wolfram.server.model.GameCreationResult
import ru.wolfram.server.model.Label
import ru.wolfram.server.model.UserCreationResult
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@RestController
@RequestMapping(MainController.PREFIX)
class MainController {
    private val users = ConcurrentHashMap.newKeySet<String>()
    private val userToKey = ConcurrentHashMap<String, String>()
    private val pending = object {
        private val lock = ReentrantLock()
        private val users = mutableListOf<String>()
        private var key = ""

        fun getKeyOrNull(name: String): String? {
            lock.lock()
            if (users.size == 2) {
                return if (name !in users) {
                    lock.unlock()
                    null
                } else {
                    key.also {
                        key = ""
                        users.clear()
                        lock.unlock()
                    }
                }
            }
            if (name in users) {
                lock.unlock()
                return null
            }
            users.add(name)
            if (users.size == 1) {
                lock.unlock()
                return null
            } else {
                key = Uuid.random().toString()
                return key.also { lock.unlock() }
            }
        }
    }

    @PostMapping("/enter")
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

    @PostMapping("/leave")
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
        while (true) {
            pending.getKeyOrNull(name)?.let {
                return@randomTicTacToe ResponseEntity.ok(GameCreationResult.GameKey(name, it))
            }
        }
    }

    @GetMapping("/games")
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
    }
}