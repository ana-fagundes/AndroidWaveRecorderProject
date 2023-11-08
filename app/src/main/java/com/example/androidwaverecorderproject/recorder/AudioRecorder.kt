package com.example.androidwaverecorderproject.recorder

import java.io.File

interface AudioRecorder {

    fun startRecording(outPutFile: File)
    fun stopRecording()
}