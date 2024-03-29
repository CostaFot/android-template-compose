package com.feelsokman.androidtemplate.keyboard

import com.feelsokman.androidtemplate.domain.JsonPlaceHolderRepository
import com.feelsokman.common.coroutine.DispatcherProvider
import com.feelsokman.logging.logDebug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class KeyboardVM @Inject constructor(
    private val jsonPlaceHolderRepository: JsonPlaceHolderRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    private val _textData = MutableStateFlow(UUID.randomUUID().toString())
    val uiState: StateFlow<String>
        get() = _textData

    private val viewModelScope by lazy {
        CoroutineScope(dispatcherProvider.ui + SupervisorJob())
    }

    init {
        getTodo()
        logDebug { "Init KeyboardVM, id: ${hashCode()}" }
    }

    fun getTodo() {
        viewModelScope.launch {
            delay(5000)
            _textData.update { UUID.randomUUID().toString() }
            /*  delay(5000)
              jsonPlaceHolderRepository.getTodo(2).fold(
                  ifError = {
                      logError { it.toString() }
                  },
                  ifSuccess = {
                      logDebug { it.title }
                      _textData.update { UUID.randomUUID().toString() }
                  }
              )*/

        }
    }

    fun onCleared() {
        logDebug { "OnDestroy KeyboardVM" }
        viewModelScope.coroutineContext.cancelChildren()
    }

}
