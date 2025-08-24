package com.feelsokman.androidtemplate.keyboard.second

import com.feelsokman.androidtemplate.keyboard.CustomViewModel
import com.feelsokman.logging.logDebug
import javax.inject.Inject

class SecondScreenVM @Inject constructor() : CustomViewModel() {

    init {
        logDebug { "SecondScreenVM init ${hashCode()}" }
    }

    override fun onCleared() {
        super.onCleared()
        logDebug { "SecondScreenVM cleared ${hashCode()}" }
    }
}