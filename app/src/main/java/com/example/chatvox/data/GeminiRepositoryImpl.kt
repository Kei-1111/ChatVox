package com.example.chatvox.data

import android.content.ContentResolver
import android.util.Log
import com.example.chatvox.BuildConfig
import com.example.chatvox.data.VoicevoxDataStore.VoicevoxType.KASUKABETUMUGI
import com.example.chatvox.data.VoicevoxDataStore.VoicevoxType.SHIKOKUMETAN
import com.example.chatvox.data.VoicevoxDataStore.VoicevoxType.ZUNDAMON
import com.example.chatvox.model.Chatvox
import com.example.chatvox.model.Message
import com.example.chatvox.model.Sender
import com.example.chatvox.model.Voicevox
import com.example.chatvox.ui.screen.chat.getBitmapOrNull
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GeminiRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    private val appPreferencesRepository: AppPreferencesRepository
) : GeminiRepository {
    override val zundamon = Chatvox(
        model = GenerativeModel(
            modelName = MODEL_NAME,
            apiKey = BuildConfig.geminiApiKey
        ).startChat(),
        isSetting = false
    )

    override val shikokumetan = Chatvox(
        model = GenerativeModel(
            modelName = MODEL_NAME,
            apiKey = BuildConfig.geminiApiKey
        ).startChat(),
        isSetting = false
    )

    override val kasukabetumugi = Chatvox(
        model = GenerativeModel(
            modelName = MODEL_NAME,
            apiKey = BuildConfig.geminiApiKey
        ).startChat(),
        isSetting = false
    )

    override suspend fun settingVoicevox(
        currentVoicevox: Voicevox
    ): Message? {
        var respons: Message? = Message(
            text = currentVoicevox.errorMessage,
            sender = Sender.VOICEVOX
        )
        val prompt = generatePrompt(currentVoicevox)

        try {
            when (currentVoicevox.voicevoxType) {
                ZUNDAMON -> {
                    if (!zundamon.isSetting) {
                        respons = respons?.copy(
                            text = zundamon.model.sendMessage(prompt).text,
                        )
                        zundamon.isSetting = true
                    } else {
                        respons = null
                    }
                }

                SHIKOKUMETAN -> {
                    if (!shikokumetan.isSetting) {
                        respons = respons?.copy(
                            text = shikokumetan.model.sendMessage(prompt).text,
                        )
                        shikokumetan.isSetting = true
                    } else {
                        respons = null
                    }
                }

                KASUKABETUMUGI -> {
                    if (!kasukabetumugi.isSetting) {
                        respons = respons?.copy(
                            text = kasukabetumugi.model.sendMessage(prompt).text,
                        )
                        kasukabetumugi.isSetting = true
                    } else {
                        respons = null
                    }
                }

            }
        } catch (e: Exception) {
            Log.d("GeminiRepositoryImpl settingVoicevox", "ERROR: $e")
        }
        return respons?.copy(
            text = respons.text?.trimIndent()
        )
    }

    override suspend fun sendMessage(
        currentVoicevox: Voicevox,
        message: Message
    ): Message {
        var respons = Message(
            text = "Null",
            sender = Sender.VOICEVOX
        )
        try {
            when {
                message.text != null -> {
                    when (currentVoicevox.voicevoxType) {
                        ZUNDAMON -> {
                            respons = respons.copy(
                                text = zundamon.model.sendMessage(message.text).text
                            )
                        }

                        SHIKOKUMETAN -> {
                            respons = respons.copy(
                                text = shikokumetan.model.sendMessage(message.text).text
                            )
                        }

                        KASUKABETUMUGI -> {
                            respons = respons.copy(
                                text = kasukabetumugi.model.sendMessage(message.text).text
                            )
                        }
                    }
                }

                message.imagePath != null -> {
                    message.imagePath.getBitmapOrNull(contentResolver)?.let {
                        when (currentVoicevox.voicevoxType) {
                            ZUNDAMON -> {
                                respons = respons.copy(
                                    text = zundamon.model.sendMessage(it).text,
                                )
                            }

                            SHIKOKUMETAN -> {
                                respons = respons.copy(
                                    text = shikokumetan.model.sendMessage(it).text,
                                )
                            }

                            KASUKABETUMUGI -> {
                                respons = respons.copy(
                                    text = kasukabetumugi.model.sendMessage(it).text,
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("GeminiRepositoryImpl sendMessage", "ERROR: $e")
            respons = respons.copy(
                text = currentVoicevox.errorMessage
            )
        }
        return respons.copy(
            text = respons.text?.trimIndent()?.trim()
        )
    }

    override fun resetVoicevox() {
        zundamon.isSetting = false
        shikokumetan.isSetting = false
        kasukabetumugi.isSetting = false
    }

    companion object {
        private const val MODEL_NAME = "gemini-1.5-pro"
    }

    private suspend fun generatePrompt(currentVoicevox: Voicevox): String {
        val userName = appPreferencesRepository.appSettings.first().userName
        return currentVoicevox.prompt.replace("{userName}", userName)
    }
}