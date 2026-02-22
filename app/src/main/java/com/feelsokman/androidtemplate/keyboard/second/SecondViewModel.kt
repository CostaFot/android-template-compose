package com.feelsokman.androidtemplate.keyboard.second

import com.feelsokman.androidtemplate.retain.RetainedViewModel
import com.feelsokman.logging.logDebug
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SecondViewModelEntryPoint {
    fun secondViewModel(): SecondViewModel
}

class SecondViewModel @Inject constructor() : RetainedViewModel() {

    init {
        logDebug { "SecondViewModel init ${hashCode()}" }
    }

    override fun onCleared() {
        logDebug { "SecondViewModel cleared ${hashCode()}" }
    }
}
