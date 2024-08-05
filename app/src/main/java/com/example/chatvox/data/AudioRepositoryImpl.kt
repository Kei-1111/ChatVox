package com.example.chatvox.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor(
    private val voicevoxRepository: VoicevoxRepository,
    private val context: Context
) : AudioRepository {
    private val cacheDir = File(context.cacheDir, "audio_cache")

    init {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }

    override suspend fun getAudioFile(
        text: String,
        speakerId: Int,
        scope: CoroutineScope,
        onFetchAudio: (Boolean) -> Unit
    ): List<Deferred<String?>> {
        val textList = text.split("。", "、", "！", "？")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { sanitizeFileName(it) }
        val audioQueue = mutableListOf<Deferred<String?>>()

        onFetchAudio(true)
        textList.forEachIndexed { index, textPart ->
            val audioDeferred = scope.async(Dispatchers.IO) {
                val cacheFile = File(cacheDir, "$textPart-$speakerId.mp3")
                if (cacheFile.exists()) {
                    return@async cacheFile.absolutePath
                } else {
                    // 一度に大量のリクエストを送るとサーバに負荷がかかるため
                    delay(index * 1000L)

                    val response = voicevoxRepository.getAudio(
                        speakerId = speakerId,
                        text = textPart
                    )

                    if (response != null && response.isSuccessful) {
                        response.body()?.byteStream()?.let { inputStream ->
                            saveAudioToFile(inputStream, cacheFile)
                            return@async cacheFile.absolutePath
                        }
                    }
                    null
                }
            }
            audioQueue.add(audioDeferred)
        }
        audioQueue[0].await()
        onFetchAudio(false)
        return audioQueue.toList()
    }

    private fun saveAudioToFile(audioStream: InputStream, file: File) {
        audioStream.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun sanitizeFileName(fileName: String): String {
        return fileName.replace(Regex("[^a-zA-Z0-9\\u3040-\\u30FF\\u4E00-\\u9FFF._-～]"), "")
    }
}