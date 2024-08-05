package com.example.chatvox.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

interface AudioRepository {
    suspend fun getAudioFile(
        text: String,
        speakerId: Int,
        scope: CoroutineScope,
        onFetchAudio: (Boolean) -> Unit = {}
    ): List<Deferred<String?>>
}