package com.feelsokman.androidtemplate.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.feelsokman.androidtemplate.ui.activity.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreenContent()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InnerMainScreenContent(
    state: String,
    onGetTodo: () -> Unit,
    onStartWork: () -> Unit,
    onCancelWork: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxSize()
    ) {
        OutlinedTextField(value = "hello", onValueChange = {

        })
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
