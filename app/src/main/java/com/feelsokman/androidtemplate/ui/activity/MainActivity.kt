package com.feelsokman.androidtemplate.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feelsokman.androidtemplate.R
import com.feelsokman.androidtemplate.ui.activity.viewmodel.MainViewModel
import com.feelsokman.common.NetworkMonitor
import com.feelsokman.design.theme.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val rr = "test lint issues"

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // Turn off the decor fitting system windows, which allows us to handle insets
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = isSystemInDarkTheme()


            AppTheme {
                DisposableEffect(systemUiController, darkTheme) {
                    systemUiController.systemBarsDarkContentEnabled = !darkTheme
                    onDispose {}
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    val startRoute = "main"
                    NavHost(navController, startDestination = startRoute) {
                        composable("main") {
                            MainRouteScreen(
                                navigate = { navController.navigate("second") }
                            )
                        }
                        composable("second") {
                            SecondRouteScreen(
                                navigate = { navController.navigate("third") }
                            )
                        }
                        composable("third") {
                            ThirdRouteScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SecondRouteScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigate: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.Green)
    ) {

        Button(
            onClick = {
                navigate()
            }
        ) {
            Text(text = "Go to third")
        }

    }
}

@Composable
fun ThirdRouteScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.Red)
    ) {

    }
}

@Composable
private fun MainRouteScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigate: () -> Unit
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    InnerMainScreenContent(
        state = state,
        navigate = navigate,
        onGetTodo = viewModel::getTodo,
        onCancelWork = viewModel::cancelWork,
        onStartWork = viewModel::startTodoWork
    )
}

@Composable
private fun InnerMainScreenContent(
    state: String,
    navigate: () -> Unit,
    onGetTodo: () -> Unit,
    onStartWork: () -> Unit,
    onCancelWork: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Text(text = "Top Center avoiding status bar", Modifier.align(Alignment.TopCenter))
        Text(text = "Bottom Center avoiding navigation bar", Modifier.align(Alignment.BottomCenter))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = modifier
                .fillMaxSize()
        ) {
            var input by remember {
                mutableStateOf("")
            }
            OutlinedTextField(
                value = input,
                onValueChange = { ff: String ->
                    input = ff
                },
                modifier = Modifier,
                label = { Text("Label") }
            )
            Text(text = state)
            Button(
                onClick = navigate,
                content = {
                    Text(text = "Go to second")
                }
            )
            Button(
                onClick = onGetTodo,
                content = {
                    Text(text = stringResource(R.string.get_todo))
                }
            )

            Button(
                onClick = onStartWork,
                content = {
                    Text(text = "Start worker")
                }
            )

            Button(
                onClick = onCancelWork,
                content = {
                    Text(text = "Cancel worker")
                }
            )

        }
    }

}
