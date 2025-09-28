package ru.wolfram.client.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import ru.wolfram.client.DispatcherIO
import ru.wolfram.client.HttpClientEngineFactory
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.data.network.common.ApiServiceImpl

@Module
class AppModule {
    @Single
    fun getHttpClient(engine: HttpClientEngine, json: Json): HttpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 30000
            socketTimeoutMillis = 30000
            requestTimeoutMillis = 30000
        }
        install(WebSockets) {
            pingIntervalMillis = 20_000
            contentConverter = KotlinxWebsocketSerializationConverter(json)
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }

    @Factory
    fun getJson(): Json {
        return Json {
            classDiscriminator = "type"
        }
    }

    @Factory
    fun getHttpClientEngine(): HttpClientEngine = HttpClientEngineFactory().getHttpClientEngine()

    @Single
    fun getDispatcherIO(): CoroutineDispatcher = DispatcherIO().getDispatcher()

    @Factory(binds = [ApiService::class])
    fun getApiService(httpClient: HttpClient) = ApiServiceImpl(httpClient)
}