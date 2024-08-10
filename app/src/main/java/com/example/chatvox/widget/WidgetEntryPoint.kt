package com.example.chatvox.widget

import com.example.chatvox.data.AppPreferencesRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun appPreferencesRepository(): AppPreferencesRepository
}