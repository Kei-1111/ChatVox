package com.example.chatvox.ui.screen.chat

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatvox.data.AudioRepository
import com.example.chatvox.data.GeminiRepository
import com.example.chatvox.data.MessageRepository
import com.example.chatvox.data.VoicevoxDataStore
import com.example.chatvox.model.AudioPlayer
import com.example.chatvox.model.Message
import com.example.chatvox.model.Sender
import com.example.chatvox.model.toMessage
import com.example.chatvox.model.toMessageEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val geminiRepository: GeminiRepository,
    private val messageRepository: MessageRepository,
    private val audioRepository: AudioRepository,
    private val audioPlayer: AudioPlayer,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState: MutableStateFlow<ChatUiState> = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    val currentVoicevox = VoicevoxDataStore.getVoicevox(
        VoicevoxDataStore.VoicevoxType.valueOf(
            savedStateHandle.get<String>("voicevoxType") ?: "ZUNDAMON"
        )
    )

    init {
        viewModelScope.launch {
            messageRepository.getMessagesByVoicevoxType(currentVoicevox.voicevoxType)
                .collect { messages ->
                    _uiState.update { currentState ->
                        currentState.copy(messageList = messages.map { it.toMessage() })
                    }
                }
        }
        viewModelScope.launch {
            setLoadingGemini(true)
            val response = geminiRepository.settingVoicevox(currentVoicevox)
            setLoadingGemini(false)
            if(_uiState.value.messageList.isEmpty()) {
                response?.let {
                    insertMessage(it)
                }
            }
        }
    }

    fun sendTextMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            val toVoicevoxMessage = Message(
                text = _uiState.value.userInput,
                sender = Sender.USER
            )
            updateUserInput("")
            insertMessage(toVoicevoxMessage)
            setLoadingGemini(true)
            val response = geminiRepository.sendMessage(
                currentVoicevox = currentVoicevox,
                message = toVoicevoxMessage
            )
            setLoadingGemini(false)
            insertMessage(response)
        }
    }

    fun sendImageMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            val toVoicevoxMessage = Message(
                imagePath = _uiState.value.imagePath,
                sender = Sender.USER
            )
            cancelImagePreview()
            insertMessage(toVoicevoxMessage)
            setLoadingGemini(true)
            val response = geminiRepository.sendMessage(
                currentVoicevox = currentVoicevox,
                message = toVoicevoxMessage
            )
            setLoadingGemini(false)
            insertMessage(response)
        }
    }

    fun playAudio(text: String) {
        viewModelScope.launch {
            val audioQueue = withContext(Dispatchers.IO) {
                audioRepository.getAudioFile(
                    text = text,
                    speakerId = currentVoicevox.speakerId,
                    scope = viewModelScope,
                    onFetchAudio = ::setLoadingVoicevoxApi
                )
            }
            audioPlayer.playAudiosSequentially(
                scope = viewModelScope,
                audioQueue = audioQueue,
                onPlayStateChange = ::setPlayingAudio
            )
        }
    }

    private fun setLoadingGemini(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoadingGemini = isLoading)
        }
    }

    private fun setLoadingVoicevoxApi(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoadingVoicevoxApi = isLoading)
        }
    }

    private fun setPlayingAudio(isPlaying: Boolean) {
        _uiState.update {
            it.copy(isPlayingAudio = isPlaying)
        }
    }


    private suspend fun insertMessage(message: Message) {
        messageRepository.insert(message.toMessageEntity(currentVoicevox.voicevoxType))
    }


    fun checkImage(imagePath: Uri) {
        _uiState.update {
            it.copy(
                imagePath = imagePath,
                isSent = true
            )
        }
    }


    fun updateUserInput(useInput: String) {
        _uiState.update {
            it.copy(userInput = useInput)
        }
    }


    fun previewImage(uri: Uri) {
        _uiState.update {
            it.copy(imagePath = uri)
        }
    }


    fun updateIsKeyboardVisible(isKeyboardVisible: Boolean) {
        _uiState.update {
            it.copy(isKeyboardVisible = isKeyboardVisible)
        }
    }


    fun cancelImagePreview() {
        _uiState.update {
            it.copy(
                imagePath = null,
                isSent = false
            )
        }
    }
}