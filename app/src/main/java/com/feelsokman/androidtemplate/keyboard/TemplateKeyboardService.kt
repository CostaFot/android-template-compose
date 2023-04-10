package com.feelsokman.androidtemplate.keyboard

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.text.TextUtils
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
class TemplateKeyboardService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

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

    @Deprecated("Deprecated in Java")
    override fun onText(text: CharSequence?) {
        currentInputConnection.commitText(text, 1)
    }

    @Deprecated("Deprecated in Java")
    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val inputConnection = currentInputConnection

        if (inputConnection != null) {
            when (primaryCode) {
                Keyboard.KEYCODE_DELETE -> {
                    val selectedText = inputConnection.getSelectedText(0)

                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0)
                    } else {
                        inputConnection.commitText("", 1)
                    }
                }
                Keyboard.KEYCODE_SHIFT -> {
                    val selectedText = inputConnection.getSelectedText(0)

                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(10000, 0)
                    } else {
                        inputConnection.commitText("", 1)
                    }
                }
                else -> {
                    val code = primaryCode.toChar()
                    inputConnection.commitText(code.toString(), 1)
                }
            }
        }
    }

    override fun onDestroy() {
        logDebug { "keyboard on onDestroy" }
        scope.coroutineContext.cancelChildren()
        super.onDestroy()
    }

    @Deprecated("Deprecated in Java")
    override fun swipeRight() {
        // NOOP
    }

    @Deprecated("Deprecated in Java")
    override fun onPress(primaryCode: Int) {
        // NOOP
    }

    @Deprecated("Deprecated in Java")
    override fun onRelease(primaryCode: Int) {
        // NOOP
    }

    @Deprecated("Deprecated in Java")
    override fun swipeLeft() {
        // NOOP
    }

    @Deprecated("Deprecated in Java")
    override fun swipeUp() {
        // NOOP
    }

    @Deprecated("Deprecated in Java")
    override fun swipeDown() {
        // NOOP
    }
}
