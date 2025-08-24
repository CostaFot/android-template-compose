package com.feelsokman.androidtemplate.keyboard

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

interface CustomViewModel {
    val viewModelScope: CoroutineScope

    fun onCleared() {
        viewModelScope.cancel()
    }
}

@Composable
inline fun <reified T : CustomViewModel> customViewModel(
    viewModelStoreOwner: CustomViewModelOwner =
        checkNotNull(LocalCustomViewModelOwner.current) {
            "No CustomViewModelOwner was provided via LocalCustomViewModelOwner"
        },
    key: Any = LocalKeyProvider.current!!
): T {
    return viewModelStoreOwner.get(key)
}