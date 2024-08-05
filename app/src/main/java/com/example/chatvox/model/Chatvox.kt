package com.example.chatvox.model

import com.google.ai.client.generativeai.Chat

data class Chatvox(
    val model: Chat,
    var isSetting: Boolean = false
)
