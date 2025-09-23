package ru.wolfram.server.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.wolfram.server.model.User
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/api/v1")
class MainController {
    private val users = ConcurrentHashMap.newKeySet<User>()

    @PostMapping("/enter")
    @MessageMapping
    fun addUser(@RequestBody user: User): ResponseEntity<String> {
        if (users.add(user)) {
            return ResponseEntity.ok("User has been added successfully!")
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User already exists!")
    }
}