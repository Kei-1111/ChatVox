package com.example.chatvox.model

import android.graphics.Bitmap
import android.net.Uri

data class Message(
    val text: String? = null,
    val imagePath: Uri? = null,
    val sender: Sender
)

//enum class Emotion(value: Int) {
//    NORMAL(0),
//    HAPPY(0),
//    ANGRY(0),
//    SAD(0),
//}


