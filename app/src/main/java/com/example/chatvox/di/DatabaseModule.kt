package com.example.chatvox.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.chatvox.data.MessageDao
import com.example.chatvox.data.MessageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile("app_settings")
        }
    )

    @Provides
    @Singleton
    fun provideMessageDatabase(
        @ApplicationContext context: Context
    ): MessageDatabase = Room.databaseBuilder(context, MessageDatabase::class.java, "message_database")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideMessageDao(
        database: MessageDatabase
    ): MessageDao = database.messageDao()
}