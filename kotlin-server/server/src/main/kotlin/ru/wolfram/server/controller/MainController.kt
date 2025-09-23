package ru.wolfram.server.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.wolfram.server.model.GameCreationResult
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
    fun addUser(@RequestParam name: String): ResponseEntity<String> {
        if (users.add(name)) {
            userToKey[name] = Uuid.random().toString()
            return ResponseEntity.ok("User has been added successfully!")
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User already exists!")
    }

    @PostMapping("/leave")
    fun leaveUser(
        @RequestParam name: String,
        @RequestParam key: String
    ): ResponseEntity<String> {
        if (userToKey[name] == key) {
            users.remove(name)
            userToKey.remove(name)
            return ResponseEntity.ok("User has been removed successfully!")
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Key is invalid or user does not exist!")
    }

    @PostMapping("/new-tic-tac-toe")
    fun newTicTacToe(@RequestParam name: String): ResponseEntity<GameCreationResult> {
        if (!users.contains(name)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(GameCreationResult.Failure("User doesn't exist!"))
        }
        return ResponseEntity.ok(GameCreationResult.GameKey(Uuid.random().toString()))
    }
}