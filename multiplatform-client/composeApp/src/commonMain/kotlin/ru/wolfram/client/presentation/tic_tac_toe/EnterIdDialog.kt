package ru.wolfram.client.presentation.tic_tac_toe

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun EnterIdDialog(
    dialogTitle: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
    val gameId = remember { mutableStateOf("") }

    AlertDialog(
        icon = {
        },
        title = {
            Text(text = dialogTitle, fontSize = 24.sp, textAlign = TextAlign.Center)
        },
        text = {
            TextField(
                value = gameId.value,
                placeholder = {
                    Text(text = "Идентификатор", fontSize = 20.sp)
                },
                onValueChange = { gameId.value = it },
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp)
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(gameId.value)
                },
            ) {
                Text(
                    text = confirmButtonText,
                    color = Color.Red,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    text = dismissButtonText,
                    color = Color.Black,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}