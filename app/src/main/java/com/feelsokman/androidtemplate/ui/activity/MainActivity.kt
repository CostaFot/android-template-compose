package com.feelsokman.androidtemplate.ui.activity

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feelsokman.androidtemplate.di.AppComponent
import com.feelsokman.androidtemplate.di.ViewModelFactory
import com.feelsokman.androidtemplate.getComponent
import com.feelsokman.common.NetworkMonitor
import com.feelsokman.design.theme.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import javax.inject.Inject
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    val rr = "test lint issues"

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations, and go edge-to-edge
        // This also sets up the initial system bar style based on the platform theme
        // enableEdgeToEdge()

        val appComponentProvider = object : AppComponentProvider {
            override val get: () -> AppComponent
                get() = {
                    application.getComponent()
                }

        }

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = isSystemInDarkTheme()

            AppTheme {
                CompositionLocalProvider(
                    LocalAppComponentProvider provides appComponentProvider,
                ) {
                    val defaultSystemBarColor = Color.Transparent
                    var systemBarStyle by remember {
                        mutableStateOf(
                            SystemBarStyle.auto(
                                lightScrim = defaultSystemBarColor.toArgb(),
                                darkScrim = defaultSystemBarColor.toArgb()
                            )
                        )
                    }
                    DisposableEffect(systemBarStyle) {
                        enableEdgeToEdge(
                            statusBarStyle = systemBarStyle,
                            navigationBarStyle = systemBarStyle
                        )
                        onDispose {}
                    }

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Green
                    ) {
                        val navController = rememberNavController()
                        NavHost(navController = navController, startDestination = "first") {
                            composable(route = "first") {
                                FirstScreen(
                                    navigate = {
                                        navController.navigate("second")
                                    },
                                )
                            }

                            composable(route = "second") {
                                SecondScreen(
                                    navigate = { navController.navigate("third") }
                                )
                            }

                            composable(route = "third") {
                                ThirdScreen(
                                    navigate = {
                                        navController.navigate("first") {
                                            popUpTo("first")
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun injectDependencies() {
        application.getComponent<AppComponent>().inject(this)
    }

}

@Composable
fun SecondMainContent(
    state: String,
    onUpdateState: () -> Unit,
    onDoSomethingElse: () -> Unit
) {
    Column {
        TextThatDisplaysState(state = state)
        FirstComposable(onUpdateState = onUpdateState)
        SecondComposable(onDoSomethingElse = onDoSomethingElse)
    }
}

@Composable
fun TextThatDisplaysState(state: String) {
    Text(text = state)
}

@Composable
private fun FirstComposable(onUpdateState: () -> Unit) {
    var ff by remember {
        mutableStateOf("hello first")
    }
    Text(text = ff)
    Button(
        onClick = {
            onUpdateState()
        }
    ) {
        Text(text = "Click me to update state")
    }
}

@Composable
private fun SecondComposable(onDoSomethingElse: () -> Unit) {

    Text(text = "hello second")
    Button(
        onClick = {
            onDoSomethingElse()
        }
    ) {
        Text(text = "frewferf")
    }
}

@Composable
fun ThirdRouteScreen(
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.Red)
    ) {

    }
}

val generateRandomColor
    get() = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))




