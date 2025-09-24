package ru.wolfram.client.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import ru.wolfram.client.DispatcherIO
import ru.wolfram.client.HttpClientEngineFactory

@Module
class AppModule {
    @Single
    fun getHttpClient(engine: HttpClientEngine, json: Json): HttpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(json)
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
}