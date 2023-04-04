package com.feelsokman.androidtemplate.keyboard

import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.feelsokman.androidtemplate.ui.theme.AppTheme
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

    composeView.setViewTreeLifecycleOwner(customLifecycleOwner)
    composeView.setViewTreeViewModelStoreOwner(customLifecycleOwner)
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
        AppTheme {
            val rememberKeyboardVM: KeyboardVM = remember {
                keyboardVM.get()
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

                    KeyboardContent(rememberKeyboardVM, it)
                }

                LaunchedEffect(pagerState) {
                    // Collect from the a snapshotFlow reading the currentPage
                    snapshotFlow { pagerState.currentPage }.collect { page ->
                        Timber.d("Page change", "Page changed to $page")
                    }
                }
            }

            LaunchedEffect(Unit) {
                Timber.tag("KeyboardComposeView").d("MasterView entered composition")
            }

            DisposableEffect(Unit) {
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

        LaunchedEffect(Unit) {
            Timber.tag("KeyboardComposeView").d("Page: $page entered composition")
        }

        DisposableEffect(Unit) {
            onDispose {
                Timber.tag("KeyboardComposeView").d("Page: $page, left composition")
                onCleared()
            }
        }
    }

}
