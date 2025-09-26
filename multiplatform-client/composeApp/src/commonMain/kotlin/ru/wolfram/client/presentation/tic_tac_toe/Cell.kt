package ru.wolfram.client.presentation.tic_tac_toe

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.wolfram.client.domain.tic_tac_toe.model.Cell

@Composable
fun Cell(
    ticTacToeViewModel: TicTacToeViewModel,
    isMove: Boolean,
    cells: List<List<Cell>>,
    size: Int,
    x: Int,
    y: Int
) {
    IconButton(
        onClick = {
            ticTacToeViewModel.handleAction(TicTacToeAction.MakeMove(x, y))
        },
        modifier = Modifier
            .size(size.dp)
            .border(2.dp, Color.Black),
        enabled = isMove
    ) {
        if (cells[y][x] != Cell.EMPTY) {
            Icon(
                imageVector = if (cells[y][x] == Cell.CROSS) {
                    Close
                } else {
                    Circle
                },
                contentDescription = null,
                modifier = Modifier.size(size.dp)
            )
        }
    }
}