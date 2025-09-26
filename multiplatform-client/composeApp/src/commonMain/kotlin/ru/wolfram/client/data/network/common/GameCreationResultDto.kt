package ru.wolfram.client.data.network.common

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = GameCreationResultSerializer::class)
sealed class GameCreationResultDto {
    @Serializable
    @JsonIgnoreUnknownKeys
    data class GameKeyDto(
        @SerialName("name") val name: String,
        @SerialName("key") val key: String
    ) : GameCreationResultDto()

    @Serializable
    @JsonIgnoreUnknownKeys
    data class FailureDto(
        @SerialName("msg") val msg: String
    ) : GameCreationResultDto()
}

object GameCreationResultSerializer : JsonContentPolymorphicSerializer<GameCreationResultDto>(
    GameCreationResultDto::class
) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<GameCreationResultDto> {
        return when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "GameKey" -> GameCreationResultDto.GameKeyDto.serializer()
            "Failure" -> GameCreationResultDto.FailureDto.serializer()
            else -> throw SerializationException("Unknown type: $type")
        }
    }
}