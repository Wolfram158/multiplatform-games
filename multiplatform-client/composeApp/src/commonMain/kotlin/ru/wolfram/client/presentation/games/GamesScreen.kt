package ru.wolfram.client.presentation.games

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.wolfram.client.domain.games.model.GamesResult
import ru.wolfram.client.domain.games.model.Label
import ru.wolfram.client.domain.games.model.Reason

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    gamesViewModel: GamesViewModel,
    onNewGameClick: (Label, Reason) -> Unit
) {
    val games = gamesViewModel.gamesState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        gamesViewModel.handleAction(GamesAction.GetGames(Locale.current.language))
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
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(start = 32.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(text = item.name, fontSize = 24.sp)
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(
                                        modifier = Modifier.fillMaxHeight().padding(end = 32.dp),
                                        verticalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(onClick = {}) {
                                            Text(text = "Create game", fontSize = 24.sp)
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(onClick = {
                                            onNewGameClick(item.label, Reason.RANDOM)
                                        }) {
                                            Text(text = "Random game", fontSize = 24.sp)
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
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