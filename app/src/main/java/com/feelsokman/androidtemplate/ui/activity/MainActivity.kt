package com.feelsokman.androidtemplate.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                                navigate = {
                                    navController.navigate("second")
                                }
                            )
                        }
                        composable("second") {
                            SecondScreen()
                        }
                        composable("third") {

                        }
                    }
                }
            }
        }
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
        login = { viewModel.login() },
        logout = { viewModel.logout() },
        update = { viewModel.updateAccount() }
    )
}

@Composable
private fun InnerMainScreenContent(
    state: MainViewModel.UiState,
    login: () -> Unit,
    logout: () -> Unit,
    update: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {

        Card(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier
                    .height(300.dp)
                    .width(300.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                when (state) {
                    MainViewModel.UiState.Loading -> {

                    }

                    is MainViewModel.UiState.LoggedIn -> {
                        LoggedInContent(state, logout, update)
                    }

                    is MainViewModel.UiState.LoggedOut -> {
                        LoggedOutContent(state, login)
                    }
                }
            }
        }

    }
}

@Composable
fun LoggedInContent(
    state: MainViewModel.UiState.LoggedIn,
    logout: () -> Unit,
    update: () -> Unit,

    ) {
    Image(
        painter = painterResource(com.feelsokman.design.R.drawable.screenshot_2023_11_12_213141),
        contentDescription = "avatar",
        contentScale = ContentScale.Crop,            // crop the image if it's not a square
        modifier = Modifier
            .size(82.dp)
            .clip(CircleShape)                       // clip to the circle shape
    )

    Text(text = state.name)
    Text(text = state.email)

    state.options.forEach {
        when (it) {
            MainViewModel.UiState.Option.LogOut -> {
                Button(
                    onClick = {
                        logout.invoke()
                    }
                ) {
                    Text(text = "Log out")
                }
            }

            MainViewModel.UiState.Option.UpdateAccount -> {
                Button(
                    onClick = {
                        update.invoke()
                    }
                ) {
                    Text(text = "Update name and email")
                }
            }


            MainViewModel.UiState.Option.LogIn -> {

            }

        }
    }

}

@Composable
fun LoggedOutContent(
    state: MainViewModel.UiState.LoggedOut,
    login: () -> Unit
) {
    Text(text = "Hello world!")

    state.options.forEach {
        when (it) {
            MainViewModel.UiState.Option.LogIn -> {
                Button(
                    onClick = {
                        login.invoke()
                    }
                ) {
                    Text(text = "Log in")
                }
            }

            MainViewModel.UiState.Option.LogOut -> {

            }


            MainViewModel.UiState.Option.UpdateAccount -> {

            }
        }
    }


}


@Composable
fun SecondScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {

}

