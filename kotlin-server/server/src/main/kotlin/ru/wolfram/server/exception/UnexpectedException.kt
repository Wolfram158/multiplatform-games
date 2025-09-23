package ru.wolfram.server.exception

class UnexpectedException(msg: String) : RuntimeException() {
    override val message = msg
}