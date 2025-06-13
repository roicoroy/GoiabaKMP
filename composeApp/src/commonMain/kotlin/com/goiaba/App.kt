package com.goiaba

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.goiaba.navigation.SetupNavGraph
import com.goiaba.shared.navigation.Screen
import com.goiaba.shared.theme.AppTypography
import com.goiaba.shared.theme.darkScheme
import com.goiaba.shared.theme.lightScheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SetupNavGraph(
                Screen.HomeGraph
            )
        }
    }
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}