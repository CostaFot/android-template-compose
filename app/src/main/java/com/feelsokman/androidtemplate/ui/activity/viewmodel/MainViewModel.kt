package com.feelsokman.androidtemplate.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.feelsokman.androidtemplate.domain.JsonPlaceHolderRepository
import com.feelsokman.androidtemplate.extensions.logDebug
import com.feelsokman.androidtemplate.extensions.logError
import com.feelsokman.androidtemplate.result.fold
import com.feelsokman.androidtemplate.work.ExpeditedGetTodoWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val jsonPlaceHolderRepository: JsonPlaceHolderRepository,
    private val workManager: WorkManager
) : ViewModel() {

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
                }
            )

        }
    }

    fun startTodoWork() {
        workManager.enqueueUniqueWork(
            "ggg",
            ExistingWorkPolicy.REPLACE,
            ExpeditedGetTodoWorker.getWorkRequest()
        )
    }

}
