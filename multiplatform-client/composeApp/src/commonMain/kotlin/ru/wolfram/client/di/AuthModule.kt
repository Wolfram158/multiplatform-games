package ru.wolfram.client.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.context.GlobalContext.get
import ru.wolfram.client.data.auth.AuthRepositoryImpl
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.domain.auth.repository.AuthRepository
import ru.wolfram.client.domain.auth.usecase.AuthUseCase
import ru.wolfram.client.presentation.auth.AuthViewModel

@Module
class AuthModule {
    @Factory(binds = [AuthRepository::class])
    fun getAuthRepository(apiService: ApiService) = AuthRepositoryImpl(
        apiService, get()
            .get<DataStore<Preferences>>()
    )

    @Factory
    fun getAuthUseCase(repository: AuthRepository) = AuthUseCase(repository)

    @KoinViewModel
    fun getAuthViewModel(authUseCase: AuthUseCase, dispatcher: CoroutineDispatcher) =
        AuthViewModel(authUseCase, dispatcher)
}