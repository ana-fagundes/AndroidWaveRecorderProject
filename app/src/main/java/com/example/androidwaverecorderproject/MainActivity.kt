package com.example.androidwaverecorderproject

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.androidwaverecorderproject.player.AudioPlayerImpl
import com.example.androidwaverecorderproject.recorder.AudioRecorderImpl
import com.example.androidwaverecorderproject.ui.theme.AndroidWaveRecorderProjectTheme
import timber.log.Timber
import java.awt.font.NumericShaper
import java.io.File
import kotlin.math.log10

class MainActivity : ComponentActivity() {

    private val recorder by lazy {
        AudioRecorderImpl()
    }

    private val player by lazy {
        AudioPlayerImpl(applicationContext)
    }

    private var audioFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )

        setContent {
            AndroidWaveRecorderProjectTheme {
                var enabledButton by remember {
                    mutableStateOf(true)
                }
                val countDownTime = recorder.countDownTime.collectAsState()
                val countDownIsRunning = recorder.countDownIsRunning.collectAsState()

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {

                            File(applicationContext.filesDir, "audio.wav").also {
                                audioFile = it
                                recorder.startRecording(it)
                            }

                            enabledButton = false
                            recorder.waveRecorder.onAmplitudeListener = {

                                val convertAmpTodB = 20 * log10(getAbsoluteValue(it))
                                println(
                                    "Amplitude: $it\n" +
                                            "dB: $convertAmpTodB \n" +
                                            "dBInt: ${
                                                convertAmpTodB.toInt()
                                            }"
                                )


                                /* if (countDownIsRunning.value == true) {
                                     if (it > 40.0) {
                                         Toast.makeText(
                                             applicationContext,
                                             "Go to quite place - $it",
                                             Toast.LENGTH_SHORT
                                         ).show()
                                         recorder.stopRecording()
                                         enabledButton = true
                                     }
                                 }
                                 if (countDownIsRunning.value == false) {

                                     if (it < 60.0) {
                                         Toast.makeText(
                                             applicationContext,
                                             "Too low - $it",
                                             Toast.LENGTH_SHORT
                                         ).show()
                                         recorder.stopRecording()
                                         enabledButton = true
                                     }

                                     if (it > 80.0) {
                                         Toast.makeText(
                                             applicationContext,
                                             "Too high - $it",
                                             Toast.LENGTH_SHORT
                                         ).show()
                                         recorder.stopRecording()
                                         enabledButton = true
                                     }
                                 }*/
                            }
                        },
                        enabled = enabledButton
                    ) {
                        Text(
                            text = if (countDownTime.value == 0) "Record"
                            else "Recording in ${countDownTime.value}"
                        )

                    }

                    Button(onClick = {
                        recorder.stopRecording()
                        enabledButton = true
                    }) {
                        Text(text = "Stop Recording")
                    }

                    Button(onClick = {
                        player.playFile(audioFile ?: return@Button)
                    }) {
                        Text(text = "Play audio")
                    }

                    Button(onClick = {
                        player.stopPlaying()
                    }) {
                        Text(text = "Stop Playing")
                    }
                }

            }
        }

    }


    companion object {
        const val MAX_AMPLITUDE = 32767.0
        const val MIN_AMPLITUDE = -32768.0
    }
}

private fun getAbsoluteValue(number: Int): Double {
    return if (number > 0) {
        number.toDouble()
    } else {
        kotlin.math.abs(number).toDouble()
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidWaveRecorderProjectTheme {
        Greeting("Android")
    }
}