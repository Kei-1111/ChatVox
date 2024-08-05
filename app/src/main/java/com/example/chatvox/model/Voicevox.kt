package com.example.chatvox.model

import androidx.annotation.DrawableRes
import com.example.chatvox.data.VoicevoxDataStore

data class Voicevox(
    val voicevoxType: VoicevoxDataStore.VoicevoxType,
    val name: String,
    val description: String,
    val credit: String,
    @DrawableRes val image: Int,
    @DrawableRes val icon: Int,
    val prompt: String,
    val speakerId: Int,
    val errorMessage: String
)
