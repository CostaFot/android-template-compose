package com.feelsokman.androidtemplate.keyboard.first

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil3.compose.AsyncImage
import com.feelsokman.androidtemplate.keyboard.customViewModel
import timber.log.Timber

@Composable
fun FirstScreen(
    goNext: () -> Unit
) {
    val firstViewModel = customViewModel<FirstViewModel>()
    val state by firstViewModel.uiState.collectAsState()

    InnerFirstScreenContent(
        state = state,
        getTodo = firstViewModel::getTodo,
        goNext = goNext
    )

    DisposableEffect(Unit) {
        onDispose {
            Timber.tag("KeyboardComposeView").d("StartScreen left composition")
        }
    }

}

@Composable
private fun InnerFirstScreenContent(
    state: String,
    getTodo: () -> Unit,
    goNext: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {

        AsyncImage(
            model = "https://cdn.betterttv.net/emote/5c548025009a2e73916b3a37/3x.webp",
            contentDescription = null,
        )
    }
}