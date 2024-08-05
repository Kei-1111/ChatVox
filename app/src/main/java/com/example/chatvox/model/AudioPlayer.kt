package com.example.chatvox.model

import android.content.Context
import android.media.MediaPlayer
import com.example.chatvox.R
import com.example.chatvox.data.VoicevoxRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class AudioPlayer @Inject constructor(
    private val context: Context,
) {
    private val mediaPlayer = MediaPlayer()

    private var playJob: Job? = null

    fun startRingtone() {
        mediaPlayer.apply {
            reset()
            val afd = context.resources.openRawResourceFd(R.raw.telephone_ringtone)
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            isLooping = true
            prepare()
            start()
        }
    }

    fun stopPlaying() {
        playJob?.cancel()

        mediaPlayer.stop()
        mediaPlayer.reset()
    }

    fun playAudiosSequentially(
        scope: CoroutineScope,
        audioQueue: List<Deferred<String?>>,
        onPlayStateChange: (Boolean) -> Unit = {},
        onPlayFinished: () -> Unit = {},
    ) {
        onPlayStateChange(true)
        playJob = scope.launch(Dispatchers.IO) {
            for (audioDeferred in audioQueue) {
                val filePath = audioDeferred.await()
                if (filePath != null) {
                    playAudio(filePath, mediaPlayer)
                    delayWhilePlaying(mediaPlayer)
                }
            }
            onPlayStateChange(false)
            onPlayFinished()
        }
    }

    private suspend fun delayWhilePlaying(mediaPlayer: MediaPlayer) {
        while (mediaPlayer.isPlaying) {
            delay(100L)
        }
    }

    private fun playAudio(filePath: String, mediaPlayer: MediaPlayer) {
        mediaPlayer.apply {
            reset()
            setDataSource(filePath)
            prepare()
            start()
        }
    }
}
