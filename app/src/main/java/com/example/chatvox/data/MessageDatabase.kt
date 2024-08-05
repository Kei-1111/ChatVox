package com.example.chatvox.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.chatvox.model.MessageEntity
import com.example.chatvox.model.SenderConverter
import com.example.chatvox.model.VoicevoxTypeConverter

@Database(entities = [MessageEntity::class], version = 2, exportSchema = false)
@TypeConverters(SenderConverter::class, VoicevoxTypeConverter::class)
abstract class MessageDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}