package ru.wolfram.server.component

import org.springframework.web.socket.WebSocketSession
import ru.wolfram.server.exception.UnexpectedException

fun WebSocketSession.getPathOrThrow(): String =
    (uri ?: throw UnexpectedException("WebSocketSession uri is null! How is it possible?")).path
