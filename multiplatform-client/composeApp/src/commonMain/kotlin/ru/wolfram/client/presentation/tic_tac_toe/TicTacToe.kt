package ru.wolfram.client.presentation.tic_tac_toe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToe(ticTacToeViewModel: TicTacToeViewModel) {
    val ticTacToe = ticTacToeViewModel.ticTacToe.collectAsState()
    val isMove = ticTacToeViewModel.isMove.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Table") })
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
            }
        }
    }
}