package com.feelsokman.androidtemplate.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import com.feelsokman.androidtemplate.domain.JsonPlaceHolderRepository
import com.feelsokman.logging.logDebug
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val jsonPlaceHolderRepository: JsonPlaceHolderRepository
) : ViewModel() {

    init {
        logDebug { this.hashCode().toString() }
    }

    private val _textData = MutableStateFlow(UUID.randomUUID().toString())
    val state: StateFlow<String>
        get() = _textData

    init {

    }

    fun updateState() {
        _textData.update { UUID.randomUUID().toString() }
    }

    fun doSomethingElse() {
        _textData.update { UUID.randomUUID().toString() }
    }

}
