package com.example.chatvox.di

import android.content.ContentResolver
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.chatvox.data.AppPreferencesRepository
import com.example.chatvox.data.AppPreferencesRepositoryImpl
import com.example.chatvox.data.AudioRepository
import com.example.chatvox.data.AudioRepositoryImpl
import com.example.chatvox.data.GeminiRepository
import com.example.chatvox.data.GeminiRepositoryImpl
import com.example.chatvox.data.MessageDao
import com.example.chatvox.data.MessageRepository
import com.example.chatvox.data.MessageRepositoryImpl
import com.example.chatvox.data.VoicevoxApi
import com.example.chatvox.data.VoicevoxRepository
import com.example.chatvox.data.VoicevoxRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideVoicevoxRepository(
        voicevoxApi: VoicevoxApi
    ): VoicevoxRepository = VoicevoxRepositoryImpl(voicevoxApi)

    @Provides
    @Singleton
    fun provideGeminiRepository(
        @ApplicationContext context: Context,
        appPreferencesRepository: AppPreferencesRepository
    ): GeminiRepository = GeminiRepositoryImpl(context.contentResolver, appPreferencesRepository)

    @Provides
    @Singleton
    fun provideAppPreferencesRepository(
        dataStore: DataStore<Preferences>
    ): AppPreferencesRepository = AppPreferencesRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideMessageRepository(
        messageDao: MessageDao
    ): MessageRepository = MessageRepositoryImpl(messageDao)

    @Provides
    @Singleton
    fun provideAudioRepository(
        voicevoxRepository: VoicevoxRepository,
        @ApplicationContext context: Context
    ): AudioRepository = AudioRepositoryImpl(voicevoxRepository, context)
}