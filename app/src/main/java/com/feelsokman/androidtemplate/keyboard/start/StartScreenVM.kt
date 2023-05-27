package com.feelsokman.androidtemplate.keyboard.start

import androidx.compose.runtime.Stable
import com.feelsokman.androidtemplate.domain.JsonPlaceHolderRepository
import com.feelsokman.androidtemplate.keyboard.FakeViewModel
import com.feelsokman.common.result.fold
import com.feelsokman.logging.logDebug
import com.feelsokman.logging.logError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

@Stable
class StartScreenVM(
    override val viewModelScope: CoroutineScope,
    private val jsonPlaceHolderRepository: JsonPlaceHolderRepository
) : FakeViewModel() {

    init {
        logDebug { hashCode().toString() }
    }

    private val _textData = MutableStateFlow(UUID.randomUUID().toString())
    val uiState: StateFlow<String>
        get() = _textData

    fun getTodo() {
        viewModelScope.launch {
            jsonPlaceHolderRepository.getTodo(2).fold(
                ifError = {
                    logError { it.toString() }
                },
                ifSuccess = {
                    logDebug { it.title }
                    _textData.update { UUID.randomUUID().toString() }
                }
            )
        }
    }
}