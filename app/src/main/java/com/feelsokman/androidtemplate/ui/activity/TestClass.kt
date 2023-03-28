package com.feelsokman.androidtemplate.ui.activity

import android.content.Context
import com.feelsokman.androidtemplate.core.coroutine.DispatcherProvider
import com.feelsokman.androidtemplate.di.ApplicationCoroutineScope
import com.feelsokman.androidtemplate.extensions.logDebug
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class TestClass @Inject constructor(
    @ApplicationContext private val appContext: Context,
    @ApplicationCoroutineScope private val applicationCoroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider
) {

    fun bla() {
        applicationCoroutineScope.launch(dispatcherProvider.io) {
            logDebug { "Hello" }
        }

    }
}
