package com.example.chatvox.ui.screen.call

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatvox.data.AudioRepository
import com.example.chatvox.data.GeminiRepository
import com.example.chatvox.data.VoicevoxDataStore
import com.example.chatvox.model.AudioPlayer
import com.example.chatvox.model.Message
import com.example.chatvox.model.Sender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    private val speechRecognizer: SpeechRecognizer,
    private val audioPlayer: AudioPlayer,
    private val audioRepository: AudioRepository,
    private val geminiRepository: GeminiRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<CallUiState> = MutableStateFlow(CallUiState())
    val uiState: StateFlow<CallUiState> = _uiState.asStateFlow()

    val currentVoicevox = VoicevoxDataStore.getVoicevox(
        VoicevoxDataStore.VoicevoxType.valueOf(
            savedStateHandle.get<String>("voicevoxType") ?: "ZUNDAMON"
        )
    )

    init {
        initializeSpeechRecognizer()
    }

    fun startCall() {
        audioPlayer.startRingtone()
        sendMessage("もしもし")
    }

    private fun initializeSpeechRecognizer() {
        speechRecognizer.setRecognitionListener(
            object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {}
                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    matches?.let {
                        sendMessage(it.firstOrNull() ?: "")
                    }
                }
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            }
        )
    }

    private fun startListening() {
        viewModelScope.launch(Dispatchers.Main) {
            speechRecognizer.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH))
        }
    }

    fun stopAudio() {
        audioPlayer.stopPlaying()
    }

    fun sendMessage(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val toVoicevoxMessage = Message(
                text = text,
                sender = Sender.USER,
            )
            setLoadingGemini(true)
            val response = geminiRepository.sendMessage(
                message = toVoicevoxMessage,
                currentVoicevox = currentVoicevox
            )
            setLoadingGemini(false)
            val audioQueue = response.text?.let {
                audioRepository.getAudioFile(
                    text = it,
                    speakerId = currentVoicevox.speakerId,
                    scope = viewModelScope,
                    onFetchAudio = ::setFetchingAudioFile
                )
            }
            audioQueue?.let {
                audioPlayer.playAudiosSequentially(
                    scope = viewModelScope,
                    audioQueue = it,
                    onPlayStateChange = ::setPlayingAudio,
                    onPlayFinished = ::startListening,
                )
            }
        }
    }

    fun stopSpeechRecognizer() {
        speechRecognizer.stopListening()
    }

    private fun setPlayingAudio(isPlayingAudio: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isPlayingAudio = isPlayingAudio)
        }
    }

    private fun setFetchingAudioFile(isFetchingAudioFile: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isFetchingAudioFile = isFetchingAudioFile)
        }
    }

    private fun setLoadingGemini(isLoadingGemini: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isLoadingGemini = isLoadingGemini)
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer.destroy()
    }
}