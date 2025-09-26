package ru.wolfram.client.data.network.auth

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
@Serializable(with = UserCreationResultSerializer::class)
sealed interface UserCreationResultDto {
    @Serializable
    @JsonIgnoreUnknownKeys
    data class UserKeyDto(
        @SerialName("name") val name: String,
        @SerialName("key") val key: String,
    ) : UserCreationResultDto

    @Serializable
    @JsonIgnoreUnknownKeys
    data class FailureDto(
        @SerialName("msg") val msg: String,
    ) : UserCreationResultDto
}

object UserCreationResultSerializer : JsonContentPolymorphicSerializer<UserCreationResultDto>(
    UserCreationResultDto::class
) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<UserCreationResultDto> {
        return when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "UserKey" -> UserCreationResultDto.UserKeyDto.serializer()
            "Failure" -> UserCreationResultDto.FailureDto.serializer()
            else -> throw SerializationException("Unknown type: $type")
        }
    }
}