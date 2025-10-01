package ru.wolfram.client

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.sp
import java.lang.Exception
import kotlin.test.Test


class ComposeAppCommonTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun example() = runComposeUiTest {
        setContent {
            Testing()
        }

        onNodeWithTag("x").performTextInput("7")
        onNodeWithTag("y").performTextInput("62")
        onNodeWithTag("click").performClick()
        onNodeWithTag("z").assertTextEquals("69")
    }
}

@Composable
fun Testing() {
    val x = remember { mutableIntStateOf(0) }
    val y = remember { mutableIntStateOf(0) }
    val z = remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = "",
            placeholder = {
                Text(text = "x", fontSize = 20.sp)
            },
            onValueChange = {
                try {
                    x.intValue = it.toInt()
                } catch (_: Exception) {

                }
            },
            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
            modifier = Modifier.testTag("x")
        )
        TextField(
            value = "",
            placeholder = {
                Text(text = "y", fontSize = 20.sp)
            },
            onValueChange = {
                try {
                    y.intValue = it.toInt()
                } catch (_: Exception) {

                }
            },
            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
            modifier = Modifier.testTag("y")
        )
        Text(z.intValue.toString(), modifier = Modifier.testTag("z"))
        Button(onClick = {
            z.intValue = x.intValue + y.intValue
        }, modifier = Modifier.testTag("click")) {
            Text("Click")
        }
    }
}