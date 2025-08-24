package com.feelsokman.androidtemplate.keyboard.second

import com.feelsokman.androidtemplate.keyboard.CustomViewModel
import com.feelsokman.logging.logDebug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

class SecondScreenVM @Inject constructor() : CustomViewModel {

    override val viewModelScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)


    init {
        logDebug { "SecondScreenVM init ${hashCode()}" }
    }

    override fun onCleared() {
        super.onCleared()
        logDebug { "SecondScreenVM cleared ${hashCode()}" }
    }
}