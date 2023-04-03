package com.feelsokman.androidtemplate.keyboard

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.compositionContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.feelsokman.androidtemplate.R
import com.feelsokman.androidtemplate.core.compose.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Provider

fun createKeyboardComposeView(
    service: TemplateKeyboardService,
    keyboardVM: Provider<KeyboardVM>
): ComposeView {
    val composeView = ComposeView(service)
    val customLifecycleOwner = CustomLifecycleOwner()

    // composeView needs all these as we are not in the context of an activity/fragment
    composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)

    customLifecycleOwner.performRestore(null)

    customLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

    ViewTreeLifecycleOwner.set(composeView, customLifecycleOwner)

    ViewTreeViewModelStoreOwner.set(composeView) { customLifecycleOwner.viewModelStore }

    composeView.setViewTreeSavedStateRegistryOwner(customLifecycleOwner)

    val coroutineContext = AndroidUiDispatcher.CurrentThread

    val runRecomposeScope = CoroutineScope(coroutineContext)

    val recomposer = Recomposer(coroutineContext)

    composeView.compositionContext = recomposer

    runRecomposeScope.launch {
        recomposer.runRecomposeAndApplyChanges()
    }.invokeOnCompletion {
        Timber.tag("KeyboardComposeView").d("Recomposer on completion")
    }

    composeView.setMainContent(keyboardVM)

    return composeView
}

private fun ComposeView.setMainContent(
    keyboardVM: Provider<KeyboardVM>
) {

    id = R.id.keyboardComposeView
    addOnAttachStateChangeListener(
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                Timber.tag("KeyboardComposeView").d("onViewAttachedToWindow")
            }

            override fun onViewDetachedFromWindow(v: View) {
                Timber.tag("KeyboardComposeView").d("onViewDetachedFromWindow")
            }
        }
    )

    setContent {
        MaterialTheme {
            val rememberKeyboardVM: KeyboardVM = remember {
                keyboardVM.get()
            }

            KeyboardContent(rememberKeyboardVM)
        }

        OnLifecycleEvent { _, event ->
            Timber.tag("KeyboardComposeView").d("Compose lifecycle: ${event.name}")
        }
    }

}

@Composable
fun KeyboardContent(keyboardVM: KeyboardVM) {
    val state by keyboardVM.uiState.collectAsState()
    InnerKeyboardContent(
        state = state,
        onClick = keyboardVM::getTodo
    )
}

@Composable
fun InnerKeyboardContent(state: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Text(text = "Hello from Custom Keyboard")
        Text(text = "This is a custom ComposeView")
        Text(text = state)
        Button(
            onClick = {
                onClick()
            }
        ) {
            Text(text = "Click me")
        }
    }
}
