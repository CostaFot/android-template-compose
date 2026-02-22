package com.feelsokman.androidtemplate.keyboard.first

import com.feelsokman.androidtemplate.keyboard.KeyboardHandler
import com.feelsokman.androidtemplate.keyboard.KeyboardMessage
import com.feelsokman.androidtemplate.retain.RetainedViewModel
import com.feelsokman.logging.logDebug
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import javax.inject.Inject

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FirstViewModelEntryPoint {
    fun firstViewModel(): FirstViewModel
}

class FirstViewModel @Inject constructor(
    private val keyboardHandler: KeyboardHandler,
) : RetainedViewModel() {

    init {
        logDebug { "FirstViewModel init ${hashCode()}" }
    }

    override fun onCleared() {
        logDebug { "FirstViewModel cleared ${hashCode()}" }
    }

    fun onText(text: String) {
        viewModelScope.launch {
            keyboardHandler.queue.emit(KeyboardMessage.Text(text))
        }
    }
}
