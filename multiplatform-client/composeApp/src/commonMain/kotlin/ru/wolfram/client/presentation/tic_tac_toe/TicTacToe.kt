package ru.wolfram.client.presentation.tic_tac_toe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import games.composeapp.generated.resources.Res
import games.composeapp.generated.resources.back
import org.jetbrains.compose.resources.vectorResource
import ru.wolfram.client.BackHandle
import ru.wolfram.client.domain.common.isEnd
import ru.wolfram.client.domain.tic_tac_toe.model.Error
import ru.wolfram.client.domain.tic_tac_toe.model.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToe(ticTacToeViewModel: TicTacToeViewModel, navHostController: NavHostController) {
    val ticTacToe = ticTacToeViewModel.ticTacToe.collectAsState()
    val title = remember { mutableStateOf("") }
    val isMove = ticTacToeViewModel.isMove.collectAsState()
    val error = ticTacToeViewModel.error.collectAsState()
    val isDialogOpened = rememberSaveable { mutableStateOf(false) }

    title.value = if (ticTacToe.value.state == State.INITIAL) {
        "Ожидание противника"
    } else if (ticTacToe.value.state == State.WIN_FAILURE) {
        "Игрок ${ticTacToe.value.winner} одержал победу!"
    } else if (ticTacToe.value.state == State.DRAW) {
        "Никто не одержал победу!"
    } else if (ticTacToeViewModel.side == ticTacToe.value.whoseMove) {
        "Ваш ход!"
    } else {
        "Ход противника!"
    }

    if (isDialogOpened.value) {
        BackDialog(
            dialogTitle = "Вы уверены, что желаете завершить процесс досрочно?",
            confirmButtonText = "Да",
            dismissButtonText = "Нет",
            onDismissRequest = {
                isDialogOpened.value = false
            },
            onConfirmation = {
                navHostController.popBackStack()
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title.value) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (!ticTacToe.value.isEnd()) {
                                isDialogOpened.value = true
                            }
                        },
                        enabled = !isDialogOpened.value
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.back),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { it ->
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().padding(it),
            contentAlignment = Alignment.Center,
            propagateMinConstraints = true
        ) {
            val size = minOf(maxHeight, maxWidth) / 3 - 16.dp
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (error.value) {
                    Error.Empty -> {
                        if (ticTacToe.value.cells.isNotEmpty()) {
                            repeat(3) { y ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    repeat(3) { x ->
                                        Cell(
                                            ticTacToeViewModel,
                                            ticTacToe.value.state,
                                            isMove.value,
                                            ticTacToe.value.cells,
                                            size,
                                            x,
                                            y
                                        )
                                    }
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(size))
                            }
                        }
                    }

                    Error.UnexpectedEnd -> {
                        Error("Произошла ошибка при загрузке данных!", size)
                    }

                    Error.OpponentNotFound -> {
                        Error(
                            "Не удалось найти противника!",
                            size,
                            retryText = "Попробовать снова"
                        ) {
                            ticTacToeViewModel.handleAction(TicTacToeAction.Retry)
                        }
                    }
                }
            }
            BackHandle {
                isDialogOpened.value = true
            }
        }
    }
}