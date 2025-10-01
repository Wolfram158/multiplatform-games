package ru.wolfram.client.presentation.tic_tac_toe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import games.composeapp.generated.resources.Res
import games.composeapp.generated.resources.warn
import org.jetbrains.compose.resources.vectorResource

@Composable
fun Error(text: String, size: Dp, retryText: String? = null, onRetry: (() -> Unit)? = null) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = vectorResource(Res.drawable.warn),
            contentDescription = null,
            modifier = Modifier.size(size),
            tint = Color.Red
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
        retryText?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    onRetry?.invoke()
                }
            ) {
                Text(retryText, fontSize = 24.sp, textAlign = TextAlign.Center)
            }
        }
    }
}