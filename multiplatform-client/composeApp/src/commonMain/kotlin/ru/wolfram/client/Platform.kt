package ru.wolfram.client

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform