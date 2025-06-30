package com.example.yourdietbuddy.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

// Light color scheme custom dari BMICalculator
private val BMICalculatorLightColors = lightColorScheme(
    primary = PurpleDark,
    secondary = PurpleLight,
    background = CardBg,
    onPrimary = ButtonText,
    onBackground = TextPrimary,
)

// Light color scheme dari YourDietBuddy
private val YourDietBuddyLightColors = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

// Dark color scheme dari YourDietBuddy
private val YourDietBuddyDarkColors = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    useBMICalculatorColors: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        useBMICalculatorColors -> BMICalculatorLightColors
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> YourDietBuddyDarkColors
        else -> YourDietBuddyLightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes(), // sesuai yang kau punya
        content = content
    )
}
