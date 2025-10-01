package ru.wolfram.server.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread
import kotlin.test.assertFalse

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

    @Test
    fun `GIVEN 1001 users WHEN they want to get key and one cancels THEN 500 distinct keys`() {
        val usersCount = 1001
        val keys = ConcurrentHashMap.newKeySet<String>()
        var i = 0
        buildList {
            repeat(usersCount) { name ->
                add(thread {
                    val key = pending.getKey(name.toString())
                    keys.add(key)
                })
            }
        }.forEach { thread ->
            if (++i == usersCount) {
                return@forEach
            }
            thread.join()
        }
        Thread.sleep(1000)
        assertEquals(usersCount / 2, keys.size)
        assertFalse(pending.isEmpty())
        pending.deleteUserIfExists((usersCount - 1).toString())
        assertTrue(pending.isEmpty())
    }
}