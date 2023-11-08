package com.example.androidwaverecorderproject.player

import java.io.File

interface AudioPlayer {

    fun playFile(file: File)
    fun stopPlaying()

}