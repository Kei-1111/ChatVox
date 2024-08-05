package com.example.chatvox.data

import okhttp3.ResponseBody
import retrofit2.Response

interface VoicevoxRepository {
    suspend fun getAudio(speakerId: Int, text: String): Response<ResponseBody>?
}