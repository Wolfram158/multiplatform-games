package ru.wolfram.client.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.wolfram.client.domain.auth.model.UserCreationResult
import ru.wolfram.client.presentation.common.Error

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onSuccess: () -> Unit
) {
    val auth = authViewModel.authState.collectAsStateWithLifecycle()
    val usedName = authViewModel.name.collectAsStateWithLifecycle()
    val name = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        authViewModel.handleAction(AuthAction.GetAlreadyUsedName)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = name.value,
                onValueChange = {
                    name.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                enabled = auth.value !is UserCreationResult.Progress && auth.value !is UserCreationResult.UserKey,
                placeholder = {
                    Text(text = "Enter name")
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    authViewModel.handleAction(AuthAction.Auth(name.value))
                },
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                enabled = auth.value !is UserCreationResult.Progress && auth.value !is UserCreationResult.UserKey
            ) {
                Text(text = "Enter", fontSize = 20.sp, textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (usedName.value != null) {
                val name = usedName.value
                if (name != null) {
                    Button(
                        onClick = {
                            authViewModel.handleAction(AuthAction.Login)
                        },
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                        enabled = auth.value !is UserCreationResult.Progress && auth.value !is UserCreationResult.UserKey
                    ) {
                        Text(
                            text = "Попробовать войти как $name",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            when (auth.value) {
                is UserCreationResult.Failure -> {
                    Error("Произошла ошибка!", 36.dp, modifier = Modifier.fillMaxWidth())
                }

                UserCreationResult.Initial -> {
                }

                is UserCreationResult.UserKey -> {
                    onSuccess()
                }

                UserCreationResult.Progress -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun AuthScreenPreview() {
    AuthScreen(koinViewModel<AuthViewModel>(), {})
}