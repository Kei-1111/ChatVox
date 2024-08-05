package com.example.chatvox.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.chatvox.data.VoicevoxDataStore

@Entity(tableName = "message_table")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val text: String? = null,

    val imagePath: String? = null,

    val sender: Sender,

    @ColumnInfo(name = "voicevox_type")
    val voicevoxType: VoicevoxDataStore.VoicevoxType
)

object SenderConverter {
    @TypeConverter
    fun fromSender(sender: Sender): String {
        return sender.name
    }

    @TypeConverter
    fun toSender(sender: String): Sender {
        return Sender.valueOf(sender)
    }
}

object VoicevoxTypeConverter {
    @TypeConverter
    fun fromVoicevoxType(voicevoxType: VoicevoxDataStore.VoicevoxType?): String? {
        return voicevoxType?.name
    }

    @TypeConverter
    fun toVoicevoxType(voicevoxTypeName: String?): VoicevoxDataStore.VoicevoxType? {
        return voicevoxTypeName?.let { VoicevoxDataStore.VoicevoxType.valueOf(it) }
    }
}

fun Message.toMessageEntity(voicevoxType: VoicevoxDataStore.VoicevoxType): MessageEntity {
    return MessageEntity(
        text = text,
        imagePath = imagePath.toString(),
        sender = sender,
        voicevoxType = voicevoxType
    )
}

fun MessageEntity.toMessage(): Message {
    return Message(
        text = text,
        imagePath = Uri.parse(imagePath),
        sender = sender
    )
}