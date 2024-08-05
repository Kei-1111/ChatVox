package com.example.chatvox.ui.component

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.example.chatvox.R
import com.example.chatvox.model.Message
import com.example.chatvox.ui.theme.chatTextShape

@Composable
fun ChatVoxUserMessage(
    message: Message,
    checkImage: (Uri) -> Unit
) {
    
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        when {
            message.text != null -> {
                Text(
                    text = message.text,
                    modifier = Modifier
                        .sizeIn(
                            maxWidth = dimensionResource(id = R.dimen.max_chat_text_width)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = chatTextShape.userTextShape
                        )
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.medium_padding),
                            vertical = dimensionResource(id = R.dimen.extra_small_padding)
                        ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            message.imagePath != null -> {
                Image(
                    painter = rememberAsyncImagePainter(message.imagePath),
                    contentDescription = "Image",
                    modifier = Modifier
                        .sizeIn(
                            maxWidth = dimensionResource(id = R.dimen.max_chat_image_width),
                            maxHeight = dimensionResource(id = R.dimen.max_chat_image_height)
                        )
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { checkImage(message.imagePath) },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}