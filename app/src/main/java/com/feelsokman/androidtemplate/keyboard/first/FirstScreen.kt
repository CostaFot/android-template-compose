package com.feelsokman.androidtemplate.keyboard.first

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.feelsokman.androidtemplate.keyboard.customViewModel
import timber.log.Timber

@Composable
fun FirstScreen(
    goNext: () -> Unit
) {
    val firstViewModel = customViewModel<FirstViewModel>()
    val state by firstViewModel.uiState.collectAsState()

    InnerFirstScreenContent(
        state = state,
        getTodo = firstViewModel::getTodo,
        goNext = goNext
    )

    DisposableEffect(Unit) {
        onDispose {
            Timber.tag("KeyboardComposeView").d("StartScreen left composition")
        }
    }

}

@Composable
private fun InnerFirstScreenContent(
    state: String,
    getTodo: () -> Unit,
    goNext: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    val keyboardLayout = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Z", "X", "C", "V", "B", "N", "M", "Backspace"),
        listOf("Space")
    )

    Box(
        Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6A11CB), // Purple
                        Color(0xFF2575FC)  // Blue
                    )
                )
            )
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets.systemBars.only(
                        WindowInsetsSides.Bottom
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Tab", "Ctrl", "Alt", "Shift").forEachIndexed { index, keyText ->
                    when (keyText) {
                        "Tab" -> {
                            Button(
                                onClick = { /* Handle click */ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .padding(horizontal = 4.dp)
                                    .clip(RoundedCornerShape(12.dp)), // More rounded corners
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF34495E), // Wet Asphalt - a darker gray
                                    contentColor = Color.White
                                ),
                                border = BorderStroke(1.dp, Color.LightGray),
                                contentPadding = ButtonDefaults.TextButtonContentPadding // Ensure text is centered
                            ) {
                                AsyncImage(
                                    model = "https://cdn.betterttv.net/emote/5c548025009a2e73916b3a37/3x.webp",
                                    contentDescription = "Tab Icon",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        "Ctrl" -> {
                            Button(
                                onClick = { /* Handle click */ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .padding(horizontal = 4.dp)
                                    .clip(RoundedCornerShape(12.dp)), // More rounded corners
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF34495E), // Wet Asphalt - a darker gray
                                    contentColor = Color.White
                                ),
                                border = BorderStroke(1.dp, Color.LightGray),
                                contentPadding = ButtonDefaults.TextButtonContentPadding // Ensure text is centered
                            ) {
                                AsyncImage(
                                    model = "https://cdn.betterttv.net/emote/5f1b0186cf6d2144653d2970/3x.webp",
                                    contentDescription = "Ctrl Icon"
                                )
                            }
                        }

                        "Alt" -> {
                            Button(
                                onClick = { /* Handle click */ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .padding(horizontal = 4.dp)
                                    .clip(RoundedCornerShape(12.dp)), // More rounded corners
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF34495E), // Wet Asphalt - a darker gray
                                    contentColor = Color.White
                                ),
                                border = BorderStroke(1.dp, Color.LightGray),
                                contentPadding = ButtonDefaults.TextButtonContentPadding // Ensure text is centered
                            ) {
                                AsyncImage(
                                    model = "https://cdn.betterttv.net/emote/5ada077451d4120ea3918426/3x.webp",
                                    contentDescription = "Ctrl Icon"
                                )
                            }
                        }

                        "Shift" -> {
                            Button(
                                onClick = { /* Handle click */ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .padding(horizontal = 4.dp)
                                    .clip(RoundedCornerShape(12.dp)), // More rounded corners
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF34495E), // Wet Asphalt - a darker gray
                                    contentColor = Color.White
                                ),
                                border = BorderStroke(1.dp, Color.LightGray),
                                contentPadding = ButtonDefaults.TextButtonContentPadding // Ensure text is centered
                            ) {
                                AsyncImage(
                                    model = "https://media.tenor.com/azqdSGcAGXoAAAAM/kekw-kewkwait.gif",
                                    contentDescription = "Ctrl Icon"
                                )
                            }
                        }

                        else -> {
                            KeyboardButton(
                                text = keyText,
                                onClick = { /* Handle click */ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .padding(horizontal = 4.dp)
                            )
                        }
                    }
                    if (index < 3) {
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))


            keyboardLayout.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .height(IntrinsicSize.Min), // Ensure all buttons in a row have same height
                    verticalAlignment = Alignment.CenterVertically // Center buttons vertically
                ) {
                    row.forEachIndexed { index, key ->
                        val weight = if (key == "Space") 3f else 1f // Define weight here
                        KeyboardButton(
                            text = key,
                            onClick = {
                                when (key) {
                                    "Backspace" -> if (text.isNotEmpty()) text = text.dropLast(1)
                                    "Space" -> text += " "
                                    else -> text += key
                                }
                            },
                            modifier = Modifier
                                .weight(weight) // Apply weight
                                .height(50.dp)
                                .padding(2.dp) // Add padding for consistent spacing
                        )
                        if (index < row.size - 1) {
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KeyboardButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)), // More rounded corners
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF34495E), // Wet Asphalt - a darker gray
            contentColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        contentPadding = ButtonDefaults.TextButtonContentPadding // Ensure text is centered
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), // Fill the button's space
            contentAlignment = Alignment.Center // Center the text within the Box
        ) {
            if (text == "Backspace") {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = Color.White
                )
            } else {
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = if (text.length > 1) 12.sp else 16.sp, // Smaller font for longer text like "Backspace"
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



