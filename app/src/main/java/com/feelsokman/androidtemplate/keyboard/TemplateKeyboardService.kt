package com.feelsokman.androidtemplate.keyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import com.feelsokman.common.coroutine.DispatcherProvider
import com.feelsokman.logging.logDebug
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TemplateKeyboardService : InputMethodService() {

    @Inject
    lateinit var dispatchers: DispatcherProvider

    @Inject
    lateinit var keyboardStateHolder: KeyboardStateHolder

    override fun onCreate() {
        logDebug { "keyboard on create" }
        super.onCreate()
    }

    override fun onCreateInputView(): View {
        logDebug { "keyboard on createInputView" }
        return createKeyboardComposeView(this, keyboardStateHolder)
    }

    override fun onDestroy() {
        logDebug { "keyboard on onDestroy" }
        super.onDestroy()
    }
}
