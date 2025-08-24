package com.feelsokman.androidtemplate.keyboard

import android.view.View
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Recomposer
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.compositionContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.navigationevent.setViewTreeNavigationEventDispatcherOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.feelsokman.androidtemplate.R
import com.feelsokman.androidtemplate.keyboard.second.SecondScreen
import com.feelsokman.androidtemplate.keyboard.start.StartScreen
import com.feelsokman.design.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber


data object RouteA

data class RouteB(val id: String)

fun createKeyboardComposeView(
    service: TemplateKeyboardService,
    keyboardViewModel: KeyboardViewModel
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
    composeView.setViewTreeNavigationEventDispatcherOwner(FakeNavigationEventDispatcherOwner)

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
        keyboardViewModel = keyboardViewModel
    )

    return composeView
}

private fun ComposeView.setMainContent(
    keyboardViewModel: KeyboardViewModel
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
        val lifecycle = LocalLifecycleOwner.current.lifecycle
        CompositionLocalProvider(
            LocalOnBackPressedDispatcherOwner provides FakeOnBackPressedDispatcherOwner(
                lifecycle
            ),
            LocalNavigationEventDispatcherOwner provides FakeNavigationEventDispatcherOwner,
            LocalCustomViewModelOwner provides keyboardViewModel,
        ) {
            AppTheme {

                Surface(
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val backStack = keyboardViewModel.backStack

                    NavDisplay(
                        entryDecorators = listOf(keyboardViewModel.customDecorator),
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = entryProvider {
                            entry<RouteA> {
                                StartScreen(
                                    goNext = {
                                        backStack.add(RouteB("123"))
                                    }
                                )
                            }
                            entry<RouteB> { key ->
                                SecondScreen(
                                    goNext = {
                                        backStack.removeLastOrNull()
                                    }
                                )
                            }
                        }
                    )


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
}


private val FakeOnBackPressedDispatcherOwner: (lifecycle: Lifecycle) -> OnBackPressedDispatcherOwner =
    {
        object : OnBackPressedDispatcherOwner {
            override val onBackPressedDispatcher = OnBackPressedDispatcher()

            override val lifecycle: LifecycleRegistry
                get() = (it as LifecycleRegistry)
        }
    }


private val FakeNavigationEventDispatcherOwner: NavigationEventDispatcherOwner =
    object : NavigationEventDispatcherOwner {
        override val navigationEventDispatcher: NavigationEventDispatcher =
            NavigationEventDispatcher()
    }


