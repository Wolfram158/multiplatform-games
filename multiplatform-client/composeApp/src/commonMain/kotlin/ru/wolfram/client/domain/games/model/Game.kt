package ru.wolfram.client.domain.games.model

data class Game(
    val name: String,
    val label: Label,
    val randomUrl: String,
    val createUrl: String
)