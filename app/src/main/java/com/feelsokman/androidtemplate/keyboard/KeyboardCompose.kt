package com.feelsokman.androidtemplate.keyboard

import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.compositionContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.feelsokman.androidtemplate.R
import com.feelsokman.design.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Provider

fun createKeyboardComposeView(
    service: TemplateKeyboardService,
    getKeyboardVM: Provider<KeyboardVM>
): ComposeView {
    val composeView = ComposeView(service)
    val customLifecycleOwner = CustomLifecycleOwner()

    // composeView needs all these as we are not in the context of an activity/fragment
    composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)

    customLifecycleOwner.performRestore(null)

    customLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

    composeView.setViewTreeLifecycleOwner(customLifecycleOwner)
    composeView.setViewTreeViewModelStoreOwner(customLifecycleOwner)
    composeView.setViewTreeSavedStateRegistryOwner(customLifecycleOwner)

    val coroutineContext = AndroidUiDispatcher.CurrentThread

    val recomposeScope = CoroutineScope(coroutineContext)

    val recomposer = Recomposer(coroutineContext)

    composeView.compositionContext = recomposer

    recomposeScope.launch {
        recomposer.runRecomposeAndApplyChanges()
    }.invokeOnCompletion {
        Timber.tag("KeyboardComposeView").d("Recomposer on completion")
    }

    composeView.setMainContent(
        getKeyboardVM = {
            getKeyboardVM.get()
        }
    )

    return composeView
}

private fun ComposeView.setMainContent(
    getKeyboardVM: () -> KeyboardVM
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
        AppTheme {
            val keyboardVM: KeyboardVM = remember {
                getKeyboardVM()
            }
            Surface(
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.background
            ) {
                val pagerState = rememberPagerState()
                HorizontalPager(
                    pageCount = 3,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) {

                    KeyboardContent(keyboardVM, it)
                }

                LaunchedEffect(pagerState) {
                    // Collect from the a snapshotFlow reading the currentPage
                    snapshotFlow { pagerState.currentPage }.collect { page ->
                        Timber.d("Page change", "Page changed to $page")
                    }
                }
            }

            DisposableEffect(Unit) {
                Timber.tag("KeyboardComposeView").d("MasterView entered composition")
                onDispose {
                    Timber.tag("KeyboardComposeView").d("MasterView left composition")
                }
            }
        }
    }
}

@Composable
fun KeyboardContent(keyboardVM: KeyboardVM, page: Int) {
    val state by keyboardVM.uiState.collectAsState()
    InnerKeyboardContent(
        state = state,
        page = page,
        onClick = keyboardVM::getTodo,
        onCleared = keyboardVM::onCleared
    )
}

@Composable
fun InnerKeyboardContent(
    state: String,
    page: Int,
    onClick: () -> Unit,
    onCleared: () -> Unit
) {

    Box(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Button(
                onClick = {
                    onClick()
                }
            ) {
                Text(text = "Click me")
            }
            Text(text = "Hello from Custom Keyboard")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = "This is a custom ComposeView")
            Text(text = state)

        }
    }

    DisposableEffect(Unit) {
        Timber.tag("KeyboardComposeView").d("Page: $page entered composition")
        onDispose {
            Timber.tag("KeyboardComposeView").d("Page: $page, left composition")
            onCleared()
        }
    }

}
