package com.feelsokman.androidtemplate.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.feelsokman.androidtemplate.ui.activity.viewmodel.MainViewModel
import com.feelsokman.androidtemplate.ui.theme.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
                    MainScreenContent()
                }
            }
        }
    }
}

@Composable
private fun MainScreenContent(viewModel: MainViewModel = viewModel()) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    InnerMainScreenContent(
        state = state,
        onGetTodo = viewModel::getTodo,
        onCancelWork = viewModel::cancelWork,
        onStartWork = viewModel::startTodoWork
    )
}

@Composable
private fun InnerMainScreenContent(
    state: String,
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
                onClick = onGetTodo,
                content = {
                    Text(text = "Get TODO")
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
