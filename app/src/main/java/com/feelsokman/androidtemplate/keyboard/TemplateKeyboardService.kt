package com.feelsokman.androidtemplate.keyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import com.feelsokman.common.coroutine.DispatcherProvider
import com.feelsokman.logging.logDebug
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class TemplateKeyboardService : InputMethodService() {

    @Inject
    lateinit var dispatchers: DispatcherProvider

    @Inject
    lateinit var keyboardVM: Provider<KeyboardVM>

    private val scope by lazy {
        CoroutineScope(dispatchers.ui + SupervisorJob())
    }

    override fun onCreate() {
        logDebug { "keyboard on create" }
        super.onCreate()
    }

    override fun onCreateInputView(): View {
        logDebug { "keyboard on createInputView" }
        return createKeyboardComposeView(this, keyboardVM)
    }

    override fun onDestroy() {
        logDebug { "keyboard on onDestroy" }
        scope.coroutineContext.cancelChildren()
        super.onDestroy()
    }
}
