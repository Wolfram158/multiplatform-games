package ru.wolfram.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import ru.wolfram.server.component.TicTacToeWebSocketHandler

@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(ticTacToeWebSocketHandler(), "/tic-tac-toe/*")
    }

    @Bean
    fun ticTacToeWebSocketHandler(): TicTacToeWebSocketHandler {
        return TicTacToeWebSocketHandler()
    }

}