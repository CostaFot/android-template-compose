package com.feelsokman.androidtemplate.keyboard.second

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.feelsokman.androidtemplate.keyboard.rememberFakeViewModel
import timber.log.Timber

@Composable
fun SecondScreen(
    goNext: () -> Unit
) {
    val secondScreenVM = rememberFakeViewModel<SecondScreenVM>()


    Text(text = "grgrggr")
    Button(
        onClick = {
            goNext()
        }
    ) {
        Text(text = "go back")
    }

    DisposableEffect(Unit) {
        onDispose {
            Timber.tag("KeyboardComposeView").d("StartScreen left composition")
        }
    }

}
