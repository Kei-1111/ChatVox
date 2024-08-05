package com.example.chatvox.ui.screen.call

data class CallUiState(
    val isLoadingGemini: Boolean = false,
    val isFetchingAudioFile: Boolean = false,
    val isPlayingAudio: Boolean = false,
)
