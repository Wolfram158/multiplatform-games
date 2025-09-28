package ru.wolfram.server.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

class PendingTest {
    private lateinit var pending: Pending

    @BeforeEach
    fun init() {
        pending = Pending()
    }

    @Test
    fun `GIVEN 1000 users WHEN they want to get key THEN 500 distinct keys`() {
        val usersCount = 1000
        val keys = ConcurrentHashMap.newKeySet<String>()
        buildList {
            repeat(usersCount) { name ->
                add(thread {
                    val key = pending.getKey(name.toString())
                    keys.add(key)
                })
            }
        }.forEach { it.join() }
        assertEquals(usersCount / 2, keys.size)
    }
}