package com.feelsokman.androidtemplate.keyboard.start

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
import com.feelsokman.androidtemplate.keyboard.rememberFakeViewModel
import timber.log.Timber

@Composable
fun StartScreen(
    goNext: () -> Unit
) {
    val startScreenVM = rememberFakeViewModel<StartScreenVM>()
    val state by startScreenVM.uiState.collectAsState()

    InnerStartScreenContent(
        state = state,
        getTodo = startScreenVM::getTodo,
        goNext = goNext
    )

    DisposableEffect(Unit) {
        onDispose {
            Timber.tag("KeyboardComposeView").d("StartScreen left composition")
        }
    }

}

@Composable
private fun InnerStartScreenContent(
    state: String,
    getTodo: () -> Unit,
    goNext: () -> Unit
) {

    Box(Modifier.fillMaxSize()) {
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