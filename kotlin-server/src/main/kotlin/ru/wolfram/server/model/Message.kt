package ru.wolfram.server.model

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = MessageSerializer::class)
sealed class Message {
    @Serializable
    data class Move(
        val x: Int,
        val y: Int,
        val key: String,
        val type: String,
    ) : Message()

    @Serializable
    data class FirstMessage(
        val name: String,
        val desired: Who,
        val type: String,
    ) : Message()
}

object MessageSerializer : JsonContentPolymorphicSerializer<Message>(
    Message::class
) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Message> {
        return when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "Move" -> Message.Move.serializer()
            "FirstMessage" -> Message.FirstMessage.serializer()
            else -> throw SerializationException("Unknown type: $type")
        }
    }
}