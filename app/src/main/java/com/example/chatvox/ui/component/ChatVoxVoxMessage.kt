package com.example.chatvox.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.example.chatvox.R
import com.example.chatvox.model.Message
import com.example.chatvox.model.Voicevox
import com.example.chatvox.ui.theme.chatTextShape

@Composable
fun ChatVoxVoxMessage(
    voicevox: Voicevox,
    message: Message,
    onClick: () -> Unit,
    isLoadingVoicevoxApi: Boolean,
    isPlayingAudio: Boolean
) {
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            Image(
                painter = painterResource(id = voicevox.icon),
                contentDescription = voicevox.credit,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
                    .size(
                        dimensionResource(id = R.dimen.medium_icon_size)
                    )
                    .clip(CircleShape),
            )
            Spacer(Modifier.width(dimensionResource(id = R.dimen.tiny_padding)))
            message.text?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .sizeIn(
                            maxWidth = dimensionResource(id = R.dimen.max_chat_text_width)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = chatTextShape.voxTextShape
                        )
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.medium_padding),
                            vertical = dimensionResource(id = R.dimen.extra_small_padding)
                        ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (isLoading && isLoadingVoicevoxApi) {
                CircularProgressIndicator(
                    modifier = Modifier.size(
                        dimensionResource(id = R.dimen.small_icon_size)
                    ),
                    strokeWidth = dimensionResource(id = R.dimen.circle_indicator_width),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            } else {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "VoiceButton",
                    modifier = Modifier
                        .clickable(
                            enabled = !isLoadingVoicevoxApi && !isPlayingAudio,
                            onClick = {
                                onClick()
                                isLoading = true
                            }
                        )
                        .size(
                            dimensionResource(id = R.dimen.small_icon_size)
                        ),
                    tint = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }

    if (isLoading && !isLoadingVoicevoxApi) {
        isLoading = false
    }
}