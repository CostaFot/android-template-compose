package com.feelsokman.androidtemplate.keyboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.feelsokman.androidtemplate.ApplicationEntryPoint
import com.feelsokman.androidtemplate.keyboard.second.SecondScreenVM
import com.feelsokman.androidtemplate.keyboard.start.StartScreenVM
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope

abstract class FakeViewModel {
    abstract val viewModelScope: CoroutineScope
}

@Composable
inline fun <reified T : FakeViewModel> rememberFakeViewModel(): T {
    val applicationContext = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()

    return remember(applicationContext, scope) {
        val applicationEntryPoint =
            EntryPointAccessors.fromApplication<ApplicationEntryPoint>(applicationContext)

        when (T::class) {
            StartScreenVM::class -> StartScreenVM(
                viewModelScope = scope,
                jsonPlaceHolderRepository = applicationEntryPoint.jsonPlaceHolderRepository()
            ) as T

            SecondScreenVM::class -> SecondScreenVM(
                viewModelScope = scope
            ) as T

            else -> throw IllegalStateException("Unknown class")
        }
    }
}