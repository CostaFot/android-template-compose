package com.feelsokman.androidtemplate.keyboard

import android.view.View
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.compositionContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.feelsokman.androidtemplate.R
import com.feelsokman.androidtemplate.keyboard.second.SecondScreen
import com.feelsokman.androidtemplate.keyboard.start.StartScreen
import com.feelsokman.design.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

fun createKeyboardComposeView(
    service: TemplateKeyboardService,
    keyboardVM: KeyboardVM
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
        keyboardVM = keyboardVM
    )

    return composeView
}

private fun ComposeView.setMainContent(
    keyboardVM: KeyboardVM
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
            val getKeyboardVM: () -> KeyboardVM = remember {
                { keyboardVM }
            }

            Surface(
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.background
            ) {
                val navController = rememberNavController()
                val startRoute = "main"
                LaunchedEffect(Unit) {
                    // Collect from the a snapshotFlow reading the currentPage
                    navController.currentBackStackEntryFlow.collect { page ->
                        Timber
                            .tag("KeyboardComposeView")
                            .d("Route changed to ${page.destination.route}")
                    }
                }
                NavHost(navController, startDestination = startRoute) {
                    composable("main") {
                        StartScreen(
                            goNext = {
                                navController.navigate("second")
                            }
                        )
                    }
                    composable("second") {
                        SecondScreen(
                            goNext = { navController.popBackStack() }
                        )
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



