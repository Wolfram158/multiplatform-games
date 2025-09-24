package ru.wolfram.client.data.network.common

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.appendEncodedPathSegments
import io.ktor.utils.io.InternalAPI
import ru.wolfram.client.baseHttpUrl

class ApiServiceImpl(
    private val httpClient: HttpClient
) : ApiService {
    @OptIn(InternalAPI::class)
    override suspend fun auth(name: String): UserCreationResultDto {
        return httpClient.post(baseHttpUrl) {
            url {
                appendEncodedPathSegments(ENTER)
            }
            parameter(NAME_QUERY, name)
        }.body<UserCreationResultDto>()
    }

    companion object {
        private const val ENTER = "enter"
        private const val NAME_QUERY = "name"
    }
}