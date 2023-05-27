package com.feelsokman.androidtemplate.keyboard.second

import com.feelsokman.androidtemplate.keyboard.FakeViewModel
import com.feelsokman.logging.logDebug
import kotlinx.coroutines.CoroutineScope

class SecondScreenVM(
    override val viewModelScope: CoroutineScope
) : FakeViewModel() {



    init {
        logDebug { "balls ${hashCode()}" }
    }
}