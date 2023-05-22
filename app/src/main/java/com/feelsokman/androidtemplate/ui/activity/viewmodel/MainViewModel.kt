package com.feelsokman.androidtemplate.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.feelsokman.androidtemplate.domain.JsonPlaceHolderRepository
import com.feelsokman.common.result.fold
import com.feelsokman.logging.logDebug
import com.feelsokman.logging.logError
import com.feelsokman.work.ExpeditedGetTodoWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val jsonPlaceHolderRepository: JsonPlaceHolderRepository,
    private val workManager: WorkManager
) : ViewModel() {

    init {
        logDebug { this.hashCode().toString() }
    }

    private val _textData = MutableStateFlow(UUID.randomUUID().toString())
    val state: StateFlow<String>
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

    fun cancelWork() {
        workManager.cancelAllWorkByTag(ExpeditedGetTodoWorker.TAG)
    }

    fun startTodoWork() {
        viewModelScope.launch {
            val oneTimeWorkRequest = ExpeditedGetTodoWorker.getWorkRequest()
            workManager.enqueueUniqueWork(
                "uniqueName",
                ExistingWorkPolicy.REPLACE,
                oneTimeWorkRequest
            )

            supervisorScope {
                launch {
                    workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id).asFlow().collect {
                        logDebug { it?.state?.name }
                        if (it.state.isFinished) {
                            logDebug { "Work finished" }
                            cancel()
                        }
                    }
                }
            }.invokeOnCompletion {
                logDebug { "${oneTimeWorkRequest.id} observation end" }
            }

        }
    }

    fun updateState() {
        _textData.update { UUID.randomUUID().toString() }
    }

    fun doSomethingElse() {
        _textData.update { UUID.randomUUID().toString() }
    }

}
