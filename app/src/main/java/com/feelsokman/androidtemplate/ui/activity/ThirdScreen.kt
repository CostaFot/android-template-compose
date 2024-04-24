package com.feelsokman.androidtemplate.ui.activity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.feelsokman.logging.logDebug
import javax.inject.Inject

@Composable
fun ThirdScreen(
    navigate: () -> Unit,
    thirdViewModel: ThirdViewModel = viewModel()

) {
    Column {
        Spacer(modifier = Modifier.height(100.dp))
        Button(
            onClick = {
                navigate.invoke()
            }
        ) {
            Text(text = "click for first")
        }
    }
}

class ThirdViewModel @Inject constructor() : ViewModel() {
    init {
        logDebug { "ThirdViewModel init" }
    }

    override fun onCleared() {
        logDebug { "ThirdViewModel onCleared" }
        super.onCleared()
    }
}