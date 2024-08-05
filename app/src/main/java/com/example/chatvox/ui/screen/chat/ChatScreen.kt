package com.example.chatvox.ui.screen.chat

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.ViewTreeObserver
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.core.content.FileProvider.getUriForFile
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.chatvox.R
import com.example.chatvox.data.VoicevoxDataStore
import com.example.chatvox.model.Sender
import com.example.chatvox.ui.component.ChatVoxTextField
import com.example.chatvox.ui.component.ChatVoxTopAppBar
import com.example.chatvox.ui.component.ChatVoxUserMessage
import com.example.chatvox.ui.component.ChatVoxVoxMessage
import com.example.chatvox.ui.navigation.Screens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    toPreviousScreen: () -> Unit,
    toCallScreen: (VoicevoxDataStore.VoicevoxType) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val view = LocalView.current
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()

    val cameraPermission = Manifest.permission.CAMERA
    val cameraPermissionState = rememberPermissionState(cameraPermission)

    val imagePickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                coroutineScope.launch {
                    val bitmap = it.getBitmapOrNull(context.contentResolver)
                    if (bitmap != null) {
                        saveBitmapToCache(context, bitmap)?.let { cacheUri ->
                            viewModel.previewImage(cacheUri)
                        }
                    }
                }
            }
        }
    )

    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = TakePictureWithUriContract()
    ) { (isSuccess, uri) ->
        if (isSuccess) {
            viewModel.previewImage(uri)
        }
    }

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            coroutineScope.launch {
                getImageUri(context).let {
                    takePhotoLauncher.launch(it)
                }
            }
        }
    }

    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = android.graphics.Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.height
            val keypadHeight = screenHeight - rect.bottom
            viewModel.updateIsKeyboardVisible(keypadHeight > screenHeight * 0.15)
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ChatVoxTopAppBar(
                title = viewModel.currentVoicevox.name,
                currentScreen = Screens.Chat,
                back = toPreviousScreen,
                action = {
                    toCallScreen(viewModel.currentVoicevox.voicevoxType)
                },
                actionEnabled = !uiState.isLoadingGemini && !uiState.isLoadingVoicevoxApi && !uiState.isPlayingAudio
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .imePadding(),
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.medium_padding)),
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(id = R.dimen.tiny_padding),
                    Alignment.Bottom
                ),
                contentPadding = PaddingValues(dimensionResource(id = R.dimen.tiny_padding))
            ) {
                items(uiState.messageList.reversed()) { item ->
                    when (item.sender) {
                        Sender.USER -> ChatVoxUserMessage(
                            message = item,
                            checkImage = viewModel::checkImage
                        )

                        Sender.VOICEVOX -> ChatVoxVoxMessage(
                            voicevox = viewModel.currentVoicevox,
                            message = item,
                            onClick = {
                                viewModel.playAudio(item.text!!)
                            },
                            isLoadingVoicevoxApi = uiState.isLoadingVoicevoxApi,
                            isPlayingAudio = uiState.isPlayingAudio
                        )
                    }
                }
            }
            if (uiState.isLoadingGemini) {
                Text(
                    text = viewModel.currentVoicevox.name + "入力中...",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.medium_padding),
                            vertical = dimensionResource(id = R.dimen.tiny_padding)
                        )
                )
            }
            Row(
                modifier = if (uiState.isKeyboardVisible) {
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainer
                        )
                        .padding(dimensionResource(id = R.dimen.small_padding))
                } else {
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainer
                        )
                        .padding(bottom = innerPadding.calculateBottomPadding())
                        .padding(dimensionResource(id = R.dimen.small_padding))
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(id = R.dimen.small_padding)
                )
            ) {
                if (!uiState.isKeyboardVisible) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera",
                        modifier = Modifier.clickable {
                            focusManager.clearFocus()
                            if (cameraPermissionState.status.isGranted) {
                                coroutineScope.launch {
                                    getImageUri(context).let {
                                        takePhotoLauncher.launch(it)
                                    }
                                }
                            } else {
                                requestCameraPermissionLauncher.launch(cameraPermission)
                            }
                        },
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Image",
                        modifier = Modifier.clickable {
                            focusManager.clearFocus()
                            imagePickLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                ChatVoxTextField(
                    modifier = Modifier
                        .weight(1f),
                    value = uiState.userInput,
                    onValueChange = viewModel::updateUserInput,
                )
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    modifier = Modifier.clickable(
                        onClick = viewModel::sendTextMessage,
                        enabled = uiState.userInput.isNotBlank()
                    ),
                    tint = if (uiState.userInput.isNotBlank() && !uiState.isLoadingGemini) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            }
        }
    }
    if (uiState.imagePath != null) {
        ImagePreview(
            imagePath = uiState.imagePath!!,
            cancel = viewModel::cancelImagePreview,
            send = viewModel::sendImageMessage,
            isSent = uiState.isSent
        )
    }
}

@Composable
private fun ImagePreview(
    imagePath: Uri,
    cancel: () -> Unit,
    send: () -> Unit,
    isSent: Boolean
) {
    var isIcon by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { isIcon = !isIcon }
            .background(MaterialTheme.colorScheme.scrim)
            .safeDrawingPadding()
    ) {
        if (isIcon) {
            IconButton(
                onClick = cancel,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
        Image(
            painter = rememberAsyncImagePainter(imagePath),
            contentDescription = "Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
        if (isIcon && !isSent) {
            IconButton(
                onClick = send,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
    }
}


class TakePictureWithUriContract : ActivityResultContract<Uri, Pair<Boolean, Uri>>() {

    private lateinit var imageUri: Uri

    @CallSuper
    override fun createIntent(context: Context, input: Uri): Intent {
        imageUri = input
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, input)
    }

    override fun getSynchronousResult(
        context: Context,
        input: Uri
    ): SynchronousResult<Pair<Boolean, Uri>>? = null

    @Suppress("AutoBoxing")
    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Boolean, Uri> {
        return (resultCode == Activity.RESULT_OK) to imageUri
    }
}

fun Uri.getBitmapOrNull(contentResolver: ContentResolver): Bitmap? {
    return kotlin.runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val source = ImageDecoder.createSource(contentResolver, this)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, this)
        }
    }.getOrNull()
}

private fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri? {
    val directory = File(context.cacheDir, "images/")
    directory.mkdirs()
    val file = File.createTempFile(
        "JPEG_${System.currentTimeMillis()}_",
        ".jpg",
        directory
    )

    return try {
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.close()
        Uri.fromFile(file)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

private fun getImageUri(context: Context): Uri {
    val directory = File(context.cacheDir, "images/")
    directory.mkdirs()
    val file = File.createTempFile(
        "JPEG_${System.currentTimeMillis()}_",
        ".jpg",
        directory
    )
    val authority = "com.example.chatvox.fileprovider"
    return getUriForFile(context, authority, file)
}