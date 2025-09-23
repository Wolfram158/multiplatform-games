package ru.wolfram.server.exception

class RulesViolationException(
    msg: String = "RulesViolationException occurred"
) : RuntimeException() {
    override val message = msg
}