package ru.wolfram.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import ru.wolfram.server.component.TicTacToeWebSocketHandler

@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(ticTacToeWebSocketHandler(), "$TIC_TAC_TOE/*")
    }

    @Bean
    fun ticTacToeWebSocketHandler(): TicTacToeWebSocketHandler {
        return TicTacToeWebSocketHandler(pathToSessions())
    }

    @Bean
    fun pathToSessions(): HashMap<String, Set<WebSocketSession>> {
        return HashMap()
    }

    companion object {
        const val TIC_TAC_TOE = "/tic-tac-toe"
    }
}