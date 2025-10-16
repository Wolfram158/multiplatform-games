package ru.wolfram.client.presentation.games

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ru.wolfram.client.domain.games.model.GamesResult
import ru.wolfram.client.domain.games.model.Label
import ru.wolfram.client.domain.games.model.Reason
import ru.wolfram.client.presentation.common.Route
import ru.wolfram.client.presentation.tic_tac_toe.EnterIdDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    gamesViewModel: GamesViewModel,
    onNewGameClick: (Label, Reason) -> Unit,
    navHostController: NavHostController
) {
    val games = gamesViewModel.gamesState.collectAsStateWithLifecycle()
    val isEnterIdDialogOpened = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        gamesViewModel.handleAction(GamesAction.GetGames(Locale.current.language))
    }

    if (isEnterIdDialogOpened.value) {
        EnterIdDialog(
            dialogTitle = "Введите идентификатор игровой сессии",
            confirmButtonText = "Продолжить",
            dismissButtonText = "Отменить",
            onDismissRequest = {
                isEnterIdDialogOpened.value = false
            },
            onConfirmation = { gameId ->
                isEnterIdDialogOpened.value = false
                navHostController.navigate(Route.TicTacToe(Reason.ENTER, gameId))
            },
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Games", fontSize = 24.sp) })
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues = it),
            contentAlignment = Alignment.Center
        ) {
            when (val res = games.value) {
                is GamesResult.Failure -> {
                    Button(onClick = {
                        gamesViewModel.handleAction(
                            GamesAction.GetGames(
                                Locale.current.language
                            )
                        )
                    }) {
                        Text("Try again")
                    }
                }

                is GamesResult.Games -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(
                            items = res.games,
                            key = { item -> item.label }
                        ) { item ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 32.dp,
                                                end = 32.dp,
                                                top = 16.dp,
                                                bottom = 16.dp
                                            ),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = item.name,
                                            fontSize = 24.sp,
                                            textAlign = TextAlign.Center
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Column(modifier = Modifier.width(IntrinsicSize.Max)) {
                                            Button(
                                                onClick = {
                                                    isEnterIdDialogOpened.value = true
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RectangleShape
                                            ) {
                                                Text(
                                                    text = "Enter by game id",
                                                    fontSize = 24.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(16.dp))

                                            Button(
                                                onClick = {
                                                    onNewGameClick(item.label, Reason.NEW)
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RectangleShape
                                            ) {
                                                Text(
                                                    text = "Create game",
                                                    fontSize = 24.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(16.dp))

                                            Button(
                                                onClick = {
                                                    onNewGameClick(item.label, Reason.RANDOM)
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RectangleShape
                                            ) {
                                                Text(
                                                    text = "Random game",
                                                    fontSize = 24.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                GamesResult.Initial -> {}
                GamesResult.Progress -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}