package com.example.androidwaverecorderproject.recorder

import android.media.AudioFormat
import android.os.CountDownTimer
import com.github.squti.androidwaverecorder.WaveRecorder
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

class AudioRecorderImpl : AudioRecorder {

    lateinit var waveRecorder: WaveRecorder

    var countDownTime = MutableStateFlow(0)
    var countDownIsRunning = MutableStateFlow<Boolean?>(null)

    private val countDownTimer = object : CountDownTimer(4000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            countDownTime.value = (millisUntilFinished / 1000).toInt()
            countDownIsRunning.value = true
        }

        override fun onFinish() {
            countDownTime.value = 0
            countDownIsRunning.value = false
        }

    }


    override fun startRecording(outPutFile: File) {
        waveRecorder = WaveRecorder(outPutFile.path)
        waveRecorder.waveConfig.sampleRate = 44100
        waveRecorder.noiseSuppressorActive = false
        waveRecorder.waveConfig.channels = AudioFormat.CHANNEL_IN_STEREO
        waveRecorder.waveConfig.audioEncoding = AudioFormat.ENCODING_PCM_16BIT
        waveRecorder.startRecording()

        countDownTimer.start()

    }


    override fun stopRecording() {
        waveRecorder.stopRecording()
        countDownTimer.cancel()
    }

    /*private var recorder: MediaRecorder? = null

    private var isRecording = false

    private fun createRecorder(): MediaRecorder {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            MediaRecorder(context)
        }else{
            MediaRecorder()
        }
    }


    override fun startRecording(outPutFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128000) // 128 kbps
            setAudioSamplingRate(44100) // 44.1 kHz
            setOutputFile(FileOutputStream(outPutFile).fd)

            prepare()
            start()

            isRecording = true

            recorder = this

        }
    }

    override fun stopRecording() {
        recorder?.apply {
            stop()
            reset()
        }
        recorder = null
    }
*/

}
