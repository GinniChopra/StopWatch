package com.example.stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.stopwatch.ui.theme.StopWatchTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = Color(0xFF101010),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.background(color = Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    AppWrapper()
                }
            }
        }
    }
}




@Composable
fun Timer(
    // total time of the timer
    totalTime: Long,
    modifier: Modifier = Modifier,
    initialValue: Float = 1f,
) {
    var serviceTimeToPlayInput by remember { mutableStateOf("") }
    var size by remember { mutableStateOf(IntSize.Zero) }
    var value by remember { mutableStateOf(initialValue) }
    var currentTime by remember { mutableStateOf(totalTime) }
    var isTimerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {

        if(currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }

    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()

    ) {

        // add value of the timer
        Text(
            text = (currentTime / 1000L).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = modifier.padding(bottom=8.dp),
            textAlign = TextAlign.Center
        )
        val scalingFactor = if (serviceTimeToPlayInput.isNotEmpty()) {
            totalTime.toFloat() / (serviceTimeToPlayInput.toLong() * 1000L).toFloat()
        } else {
            1f
        }
        val progress = if (currentTime > 0L) {
            (currentTime.toFloat() / totalTime.toFloat()) * scalingFactor
        } else {
            1f
        }
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth()
                .height(36.dp)
                .padding(top = 8.dp),
            color = Color.Green
        )
        Button(
            onClick = {
                if (currentTime <= 0L) {
                    currentTime = totalTime
                    isTimerRunning = true
                } else {
                    isTimerRunning = !isTimerRunning
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            // change button color
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (!isTimerRunning || currentTime <= 0L) {
                    Color.Green
                } else {
                    Color.Red
                }
            )
        ) {
            Text(
                // change the text of button based on values
                text =
                if (isTimerRunning && currentTime >= 0L) "Stop"
                else if (!isTimerRunning && currentTime >= 0L) "Start"
                else "Restart"
            )
        }
    }

    Column(modifier = Modifier
        .padding(horizontal = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(50.dp))
        EditTimeToPlay(
            value = serviceTimeToPlayInput,
            onValueChange = {
                serviceTimeToPlayInput = it
                currentTime = serviceTimeToPlayInput.toLong() * 1000L
            },
            editingEnabled = !isTimerRunning
        )

        Spacer(Modifier.height(50.dp))
        Row {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    isTimerRunning = false
                    currentTime = totalTime
                    value = 1f
                    serviceTimeToPlayInput = ""
                },
            ) {
                Text(text = "Reset")
            }
        }
    }
}

@Composable
fun EditTimeToPlay(
    value: String,
    onValueChange: (String) -> Unit,
    editingEnabled: Boolean
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        enabled = editingEnabled,
        label = { Text("Enter time in seconds")}
    )
}

@Composable
fun AppWrapper() {
    StopWatchTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Timer(
                totalTime = 60L * 1000L,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppWrapper()
}