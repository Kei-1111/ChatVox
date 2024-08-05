package com.example.chatvox.data

import com.example.chatvox.model.MessageEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao
) : MessageRepository {
    override suspend fun insert(messageEntity: MessageEntity) = messageDao.insert(messageEntity)

    override fun getMessagesByVoicevoxType(voicevoxType: VoicevoxDataStore.VoicevoxType): Flow<List<MessageEntity>> =
        messageDao.getMessagesByVoicevoxType(voicevoxType)
}