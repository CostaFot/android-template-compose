package com.feelsokman.androidtemplate.ui.activity

import androidx.compose.runtime.staticCompositionLocalOf
import com.feelsokman.androidtemplate.di.AppComponent

class NoOpAppComponentProvider : AppComponentProvider {

    override val get: () -> AppComponent
        get() {
            throw IllegalStateException("oops ")
        }
}

interface AppComponentProvider {
    val get: () -> AppComponent
}


val LocalAppComponentProvider = staticCompositionLocalOf<AppComponentProvider> {
    NoOpAppComponentProvider()
}