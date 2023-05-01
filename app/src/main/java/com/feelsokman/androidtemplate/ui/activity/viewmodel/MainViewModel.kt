package com.feelsokman.androidtemplate.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feelsokman.androidtemplate.domain.SomeDependency
import com.feelsokman.common.result.fold
import com.feelsokman.logging.logDebug
import com.feelsokman.logging.logError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val someDependency: SomeDependency
) : ViewModel() {

    init {
        logDebug { this.hashCode().toString() }
    }

    private val _textData = MutableStateFlow(UUID.randomUUID().toString())
    val uiState: StateFlow<String>
        get() = _textData

    fun getTodo() {
        viewModelScope.launch {
            someDependency.getTodo(2).fold(
                ifError = {
                    logError { it.toString() }
                },
                ifSuccess = {
                    logDebug { it.title }
                }
            )

        }
    }

    fun cancelWork() {
    }

    fun startTodoWork() {

    }

    override fun onCleared() {
        logDebug { "oncleared" }
        super.onCleared()
    }
}
