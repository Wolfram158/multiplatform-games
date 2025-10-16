package ru.wolfram.client.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.wolfram.client.domain.games.model.Label
import ru.wolfram.client.domain.games.model.Reason
import ru.wolfram.client.presentation.auth.AuthScreen
import ru.wolfram.client.presentation.auth.AuthViewModel
import ru.wolfram.client.presentation.common.Route
import ru.wolfram.client.presentation.games.GamesScreen
import ru.wolfram.client.presentation.games.GamesViewModel
import ru.wolfram.client.presentation.tic_tac_toe.EnterTicTacToeViewModel
import ru.wolfram.client.presentation.tic_tac_toe.NewTicTacToeViewModel
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

            composable<Route.Games> { entry ->
                val gamesViewModel = koinViewModel<GamesViewModel>()

                GamesScreen(gamesViewModel, { label, reason ->
                    when (label) {
                        Label.TIC_TAC_TOE -> {
                            navHostController.navigate(Route.TicTacToe(reason)) {}
                        }
                    }
                }, navHostController)
            }

            composable<Route.TicTacToe> { entry ->
                val key = rememberSaveable { mutableStateOf(false) }
                val route = entry.toRoute<Route.TicTacToe>()

                val ticTacToeViewModel = when (route.reason) {
                    Reason.RANDOM -> koinViewModel<TicTacToeViewModel>()
                    Reason.NEW -> koinViewModel<NewTicTacToeViewModel>()
                    Reason.ENTER -> koinViewModel<EnterTicTacToeViewModel>()
                }

                LaunchedEffect(key.value) {
                    if (ticTacToeViewModel is EnterTicTacToeViewModel) {
                        ticTacToeViewModel.launch(route.gameId!!)
                    }
                }

                TicTacToe(ticTacToeViewModel, route.reason, navHostController)
            }
        }
    }
}