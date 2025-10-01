package ru.wolfram.client.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.wolfram.client.domain.games.model.Label
import ru.wolfram.client.presentation.auth.AuthScreen
import ru.wolfram.client.presentation.auth.AuthViewModel
import ru.wolfram.client.presentation.common.Route
import ru.wolfram.client.presentation.games.GamesScreen
import ru.wolfram.client.presentation.games.GamesViewModel
import ru.wolfram.client.presentation.tic_tac_toe.TicTacToe
import ru.wolfram.client.presentation.tic_tac_toe.TicTacToeViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navHostController = rememberNavController()
        NavHost(navController = navHostController, startDestination = Route.Auth) {
            composable<Route.Auth> {
                val authViewModel = koinViewModel<AuthViewModel>()

                AuthScreen(
                    authViewModel
                ) {
                    navHostController.navigate(Route.Games) {
                        popUpTo(Route.Auth) {
                            inclusive = true
                        }
                    }
                }
            }

            composable<Route.Games> {
                val gamesViewModel = koinViewModel<GamesViewModel>()

                GamesScreen(gamesViewModel) { label, reason ->
                    when (label) {
                        Label.TIC_TAC_TOE -> {
                            navHostController.navigate(Route.TicTacToe) {}
                        }
                    }
                }
            }

            composable<Route.TicTacToe> {
                val ticTacToeViewModel = koinViewModel<TicTacToeViewModel>()

                TicTacToe(ticTacToeViewModel, navHostController)
            }
        }
    }
}