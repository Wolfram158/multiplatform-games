package ru.wolfram.client.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.wolfram.client.domain.auth.model.UserCreationResult
import ru.wolfram.client.domain.auth.usecase.AuthUseCase
import ru.wolfram.client.domain.auth.usecase.GetAlreadyUsedNameUseCase
import ru.wolfram.client.domain.auth.usecase.LoginUseCase
import ru.wolfram.client.presentation.common.ActionHandler

class AuthViewModel(
    private val authUseCase: AuthUseCase,
    private val getAlreadyUsedNameUseCase: GetAlreadyUsedNameUseCase,
    private val loginUseCase: LoginUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ActionHandler<AuthAction>, ViewModel() {
    private val _authState = MutableStateFlow<UserCreationResult>(UserCreationResult.Initial)
    val authState = _authState.asStateFlow()

    private val _name = MutableStateFlow<String?>(null)
    val name = _name.asStateFlow()

    override fun handleAction(action: AuthAction) {
        when (action) {
            is AuthAction.Auth -> reduceAuth(action)
            is AuthAction.GetAlreadyUsedName -> reduceGetName(action)
            is AuthAction.Login -> reduceLogin(action)
        }
    }

    private fun reduceAuth(action: AuthAction.Auth) {
        viewModelScope.launch(ioDispatcher) {
            _authState.update {
                UserCreationResult.Progress
            }
            _authState.update {
                authUseCase(action.name)
            }
        }
    }

    private fun reduceLogin(action: AuthAction.Login) {
        viewModelScope.launch(ioDispatcher) {
            _authState.update {
                UserCreationResult.Progress
            }
            _authState.update {
                loginUseCase()
            }
        }
    }

    private fun reduceGetName(action: AuthAction.GetAlreadyUsedName) {
        viewModelScope.launch(ioDispatcher) {
            _name.update {
                getAlreadyUsedNameUseCase()
            }
        }
    }

}