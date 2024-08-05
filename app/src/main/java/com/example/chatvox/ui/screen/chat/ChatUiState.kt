package com.example.chatvox.ui.screen.chat

import android.net.Uri
import com.example.chatvox.model.Message

data class ChatUiState(
    val messageList: List<Message> = emptyList(),
    val userInput: String = "",
    val isKeyboardVisible: Boolean = false,
    val imagePath: Uri? = null,
    val isSent: Boolean = false,
    val isLoadingGemini: Boolean = false,
    val isLoadingVoicevoxApi: Boolean = false,
    val isPlayingAudio: Boolean = false,
)
