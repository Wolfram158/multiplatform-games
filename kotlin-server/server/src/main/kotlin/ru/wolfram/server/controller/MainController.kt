package ru.wolfram.server.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.wolfram.server.model.GameCreationResult
import ru.wolfram.server.model.UserCreationResult
import java.util.concurrent.ConcurrentHashMap
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@RestController
@RequestMapping("/api/v1")
class MainController {
    private val users = ConcurrentHashMap.newKeySet<String>()
    private val userToKey = ConcurrentHashMap<String, String>()

    @PostMapping("/enter")
    fun addUser(@RequestParam name: String): ResponseEntity<UserCreationResult> {
        if (users.add(name)) {
            val key = Uuid.random().toString()
            userToKey[name] = key
            return ResponseEntity.ok(UserCreationResult.UserKey(key))
        }
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(UserCreationResult.Failure("User already exists"))
    }

    @PostMapping("/leave")
    fun leaveUser(
        @RequestParam name: String,
        @RequestParam key: String
    ): ResponseEntity<String> {
        if (users.contains(name) && userToKey[name] == key) {
            users.remove(name)
            userToKey.remove(name)
            return ResponseEntity.ok("User has been removed successfully!")
        }
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body("Key is invalid or user does not exist!")
    }

    @GetMapping("/new-tic-tac-toe")
    fun newTicTacToe(
        @RequestParam name: String,
        @RequestParam key: String
    ): ResponseEntity<GameCreationResult> {
        if (!users.contains(name) || userToKey[name] != key) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(GameCreationResult.Failure("User doesn't exist or key is invalid!"))
        }
        return ResponseEntity.ok(GameCreationResult.GameKey(Uuid.random().toString()))
    }
}