package dev.wxlf.connectrequest.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Blue = Color(0xFF3B7CB9)
val Grey = Color(0xFFF5F5F5)

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    background = Grey
)

@Composable
fun ConnectRequestTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}