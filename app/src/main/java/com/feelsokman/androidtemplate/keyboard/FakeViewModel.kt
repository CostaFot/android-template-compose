package com.feelsokman.androidtemplate.keyboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope

abstract class FakeViewModel {
    abstract val viewModelScope: CoroutineScope
}

@Composable
inline fun <reified T : FakeViewModel> rememberFakeViewModel(): T {
    val applicationContext = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()

    return remember(applicationContext, scope) {


        throw IllegalStateException("Unknown class")
    }

}