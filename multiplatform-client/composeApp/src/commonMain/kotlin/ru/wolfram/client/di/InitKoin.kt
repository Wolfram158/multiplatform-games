package ru.wolfram.client.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module
import ru.wolfram.client.dataStoreModule

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            AppModule().module,
            AuthModule().module,
            GamesModule().module,
            TicTacToeModule().module,
            dataStoreModule
        )
    }
}