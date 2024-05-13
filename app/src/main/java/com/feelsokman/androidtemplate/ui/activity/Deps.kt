package com.feelsokman.androidtemplate.ui.activity

import com.feelsokman.logging.logDebug
import javax.inject.Inject

class FirstScreenTracker {
    fun track(): Unit {
        // track something
    }
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