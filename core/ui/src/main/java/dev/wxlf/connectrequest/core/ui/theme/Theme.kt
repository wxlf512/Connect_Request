package dev.wxlf.connectrequest.core.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val Blue = Color(0xFF3B7CB9)
val Grey = Color(0xFFF5F5F5)

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    background = Grey,
    surface = Grey,
    surfaceVariant = Color.White,
)

@Composable
fun ConnectRequestTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}