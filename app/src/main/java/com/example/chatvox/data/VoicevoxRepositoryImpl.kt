package com.example.chatvox.data

import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class VoicevoxRepositoryImpl @Inject constructor(
    private val voicevoxApi: VoicevoxApi
) : VoicevoxRepository {

    override suspend fun getAudio(speakerId: Int, text: String): Response<ResponseBody>? {
        return try {
            voicevoxApi.getAudio(
                speaker = speakerId,
                text = text
            )
        } catch (e: Exception) {
            println("Exception during request: ${e.message}")
            null
        }
    }
}