package ru.wolfram.server.model

data class Game(
    val name: String,
    val label: Label,
    val randomUrl: String,
    val createUrl: String
)
