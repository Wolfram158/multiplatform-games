package ru.wolfram.server.service

import java.util.concurrent.locks.ReentrantLock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Pending {
    private val lock = ReentrantLock()
    private val users = mutableListOf<String>()
    private var key = ""

    @OptIn(ExperimentalUuidApi::class)
    private fun getKeyOrNull(name: String): String? {
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

    fun getKey(name: String): String {
        while (true) {
            getKeyOrNull(name)?.let { key ->
                return key
            }
        }
    }

    fun deleteUserIfExists(name: String) {
        lock.lock()
        if (users.contains(name)) {
            users.removeIf {
                it == name
            }
        }
        lock.unlock()
    }

    fun isEmpty(): Boolean {
        lock.lock()
        val isEmpty = users.isEmpty()
        println(users)
        lock.unlock()
        return isEmpty
    }
}