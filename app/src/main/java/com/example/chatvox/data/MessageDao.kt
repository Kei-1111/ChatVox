package com.example.chatvox.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chatvox.model.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(messageEntity: MessageEntity)

    @Query("SELECT * FROM message_table WHERE voicevox_type = :voicevoxType")
    fun getMessagesByVoicevoxType(voicevoxType: VoicevoxDataStore.VoicevoxType): Flow<List<MessageEntity>>
}