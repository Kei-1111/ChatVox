package com.example.chatvox.data

import com.example.chatvox.model.MessageEntity
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun insert(messageEntity: MessageEntity)
    fun getMessagesByVoicevoxType(voicevoxType: VoicevoxDataStore.VoicevoxType): Flow<List<MessageEntity>>
}