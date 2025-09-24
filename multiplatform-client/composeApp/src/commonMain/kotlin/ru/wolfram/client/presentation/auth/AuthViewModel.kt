package ru.wolfram.client.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.wolfram.client.domain.auth.usecase.AuthUseCase
import ru.wolfram.client.domain.model.common.UserCreationResult

class AuthViewModel(
    private val authUseCase: AuthUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _authState = MutableStateFlow<UserCreationResult>(UserCreationResult.Initial)
    val authState = _authState.asStateFlow()

    fun auth(name: String) {
        viewModelScope.launch(ioDispatcher) {
            _authState.update {
                authUseCase(name)
            }
        }
    }
}