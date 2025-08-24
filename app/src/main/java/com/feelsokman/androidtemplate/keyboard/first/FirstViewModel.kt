package com.feelsokman.androidtemplate.keyboard.first

import com.feelsokman.androidtemplate.domain.JsonPlaceHolderRepository
import com.feelsokman.androidtemplate.keyboard.CustomViewModel
import com.feelsokman.common.result.fold
import com.feelsokman.logging.logDebug
import com.feelsokman.logging.logError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


class FirstViewModel @Inject constructor(
    private val jsonPlaceHolderRepository: JsonPlaceHolderRepository
) : CustomViewModel() {

    init {
        logDebug { "FirstViewModel init ${hashCode()}" }
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

    override fun onCleared() {
        super.onCleared()
        logDebug { "FirstViewModel cleared ${hashCode()}" }
    }
}