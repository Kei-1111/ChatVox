package com.example.chatvox.data

import com.example.chatvox.BuildConfig
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VoicevoxApi {
    @GET("v2/voicevox/audio/")
    suspend fun getAudio(
        @Query("key") apiKey: String = BuildConfig.voicevoxApiKey,
        @Query("speaker") speaker: Int,
        @Query("text") text: String
    ): Response<ResponseBody>
}