package ru.wolfram.client.presentation.auth

import ru.wolfram.client.presentation.common.Action

sealed interface AuthAction : Action {
    data class Auth(val name: String) : AuthAction

    object Login : AuthAction

    object GetAlreadyUsedName : AuthAction

}