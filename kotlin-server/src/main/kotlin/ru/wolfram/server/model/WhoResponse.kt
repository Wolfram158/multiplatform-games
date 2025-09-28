package ru.wolfram.server.model

import kotlinx.serialization.Serializable

@Serializable
data class WhoResponse(
    val who: Who,
    val key: String
)
