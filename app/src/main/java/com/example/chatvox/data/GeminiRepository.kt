package com.example.chatvox.data

import com.example.chatvox.model.Chatvox
import com.example.chatvox.model.Message
import com.example.chatvox.model.Voicevox

interface GeminiRepository {
    val zundamon: Chatvox
    val shikokumetan: Chatvox
    val kasukabetumugi: Chatvox

    suspend fun settingVoicevox(
        currentVoicevox: Voicevox
    ): Message?

    suspend fun sendMessage(
        currentVoicevox: Voicevox,
        message: Message
    ): Message

    fun resetVoicevox()
}