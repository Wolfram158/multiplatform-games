package ru.wolfram.client.presentation.tic_tac_toe

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Close: ImageVector
    get() {
        if (_Close_small != null) return _Close_small!!

        _Close_small = ImageVector.Builder(
            name = "Close_small",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000))
            ) {
                moveToRelative(336f, -280f)
                lineToRelative(-56f, -56f)
                lineToRelative(144f, -144f)
                lineToRelative(-144f, -143f)
                lineToRelative(56f, -56f)
                lineToRelative(144f, 144f)
                lineToRelative(143f, -144f)
                lineToRelative(56f, 56f)
                lineToRelative(-144f, 143f)
                lineToRelative(144f, 144f)
                lineToRelative(-56f, 56f)
                lineToRelative(-143f, -144f)
                close()
            }
        }.build()

        return _Close_small!!
    }

private var _Close_small: ImageVector? = null


