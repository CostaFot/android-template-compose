package com.feelsokman.androidtemplate.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.feelsokman.androidtemplate.extensions.logDebug
import com.feelsokman.androidtemplate.extensions.logError
import com.feelsokman.androidtemplate.net.JsonPlaceHolderService
import com.feelsokman.androidtemplate.result.attempt
import com.feelsokman.androidtemplate.result.fold
import com.feelsokman.androidtemplate.work.GetTodoWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val jsonPlaceHolderService: JsonPlaceHolderService,
    private val workManager: WorkManager
) : ViewModel() {

    private val _textData = MutableStateFlow(UUID.randomUUID().toString())
    val uiState: StateFlow<String>
        get() = _textData

    fun getTodo() {
        viewModelScope.launch {
            attempt {
                jsonPlaceHolderService.getTodo(2)
            }.fold(
                ifError = {
                    logError { it.localizedMessage }
                },
                ifSuccess = {
                    logDebug { it.title }
                }
            )

        }
    }

    fun startSomeWork() {
        workManager.enqueue(GetTodoWorker.getWorkRequest())
    }

}
