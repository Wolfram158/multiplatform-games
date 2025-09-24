package ru.wolfram.client

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ru.wolfram.client.di.initKoin
import ru.wolfram.client.presentation.App

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Games",
        ) {
            App()
        }
    }
}