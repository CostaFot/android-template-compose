package com.feelsokman.androidtemplate.ui.activity

import com.feelsokman.common.FlagProvider
import com.feelsokman.logging.logDebug
import javax.inject.Inject

class FirstDependency(
    private val flagProvider: FlagProvider
) {

    init {
        logDebug { "FirstDependency init" }
    }

    fun doSomething(): Boolean = false
}

class SecondDependency @Inject constructor() {

    init {
        logDebug { "SecondDependency init" }
    }

    fun doSomething() = Unit
}

class ThirdDependency @Inject constructor() {

    init {
        logDebug { "ThirdDependency init" }
    }


    fun doSomething() = Unit
}