package com.example.chatvox.di

import android.content.Context
import android.speech.SpeechRecognizer
import com.example.chatvox.data.VoicevoxApi
import com.example.chatvox.data.VoicevoxRepository
import com.example.chatvox.model.AudioPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSpeechRecognizer(
        @ApplicationContext context: Context
    ): SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    @Provides
    @Singleton
    fun provideAudioPlayer(
        @ApplicationContext context: Context,
    ): AudioPlayer = AudioPlayer(context)
}