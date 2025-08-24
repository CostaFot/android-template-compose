package com.feelsokman.androidtemplate.keyboard.first

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Text(text = state)
            Button(
                onClick = {
                    getTodo()
                }
            ) {
                Text(text = "Get TODO")
            }

            Button(
                onClick = {
                    goNext()
                }
            ) {
                Text(text = "Navigate")
            }

            CircularProgressIndicator()

        }
    }
}