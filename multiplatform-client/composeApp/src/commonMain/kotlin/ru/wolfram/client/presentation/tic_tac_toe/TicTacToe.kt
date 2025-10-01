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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.wolfram.client.domain.tic_tac_toe.model.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToe(ticTacToeViewModel: TicTacToeViewModel) {
    val ticTacToe = ticTacToeViewModel.ticTacToe.collectAsState()
    val title = remember { mutableStateOf("") }
    val isMove = ticTacToeViewModel.isMove.collectAsState()

    if (ticTacToe.value.state == State.INITIAL) {
        title.value = "Ожидание противника"
    } else if (ticTacToe.value.state == State.WIN_FAILURE) {
        title.value = "${ticTacToe.value.winner} одержал победу!"
    } else if (ticTacToe.value.state == State.DRAW) {
        title.value = "Никто не одержал победу!"
    } else if (ticTacToeViewModel.side == ticTacToe.value.whoseMove) {
        title.value = "Ваш ход!"
    } else {
        title.value = "Ход противника!"
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(title.value) })
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
                if (ticTacToe.value.cells.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Cell(ticTacToeViewModel, isMove.value, ticTacToe.value.cells, size, 0, 0)
                        Cell(ticTacToeViewModel, isMove.value, ticTacToe.value.cells, size, 1, 0)
                        Cell(ticTacToeViewModel, isMove.value, ticTacToe.value.cells, size, 2, 0)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Cell(ticTacToeViewModel, isMove.value, ticTacToe.value.cells, size, 0, 1)
                        Cell(ticTacToeViewModel, isMove.value, ticTacToe.value.cells, size, 1, 1)
                        Cell(ticTacToeViewModel, isMove.value, ticTacToe.value.cells, size, 2, 1)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Cell(ticTacToeViewModel, isMove.value, ticTacToe.value.cells, size, 0, 2)
                        Cell(ticTacToeViewModel, isMove.value, ticTacToe.value.cells, size, 1, 2)
                        Cell(ticTacToeViewModel, isMove.value, ticTacToe.value.cells, size, 2, 2)
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
        }
    }
}