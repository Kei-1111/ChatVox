package com.example.chatvox.ui.screen.call

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatvox.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CallScreen(
    viewModel: CallViewModel = hiltViewModel(),
    toPreviousScreen: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val recordPermission = Manifest.permission.RECORD_AUDIO
    val recordPermissionState = rememberPermissionState(recordPermission)

    val recordPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            viewModel.startCall()
        } else {
            Toast.makeText(
                context,
                "Record permission denied",
                Toast.LENGTH_SHORT
            ).show()
            toPreviousScreen()
        }
    }

    BackHandler {
        toPreviousScreen()
        viewModel.stopAudio()
        viewModel.stopSpeechRecognizer()
    }

    LaunchedEffect(Unit) {
        if (!recordPermissionState.status.isGranted) {
            recordPermissionLauncher.launch(recordPermission)
        } else {
            viewModel.startCall()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    vertical = dimensionResource(id = R.dimen.top_app_bar_height) +
                            dimensionResource(id = R.dimen.medium_padding) +
                            dimensionResource(id = R.dimen.medium_padding)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(
                    dimensionResource(id = R.dimen.call_screen_voicevox_icon_size) +
                            dimensionResource(id = R.dimen.circle_indicator_width) * 2
                ),
                contentAlignment = Alignment.Center,
            ) {
                if (uiState.isLoadingGemini || uiState.isFetchingAudioFile) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(
                                dimensionResource(id = R.dimen.call_screen_voicevox_icon_size) +
                                        dimensionResource(id = R.dimen.circle_indicator_width) * 2
                            ),
                        strokeWidth = dimensionResource(id = R.dimen.circle_indicator_width)
                    )
                }
                Image(
                    painter = painterResource(id = viewModel.currentVoicevox.icon),
                    contentDescription = viewModel.currentVoicevox.credit,
                    modifier = if(uiState.isPlayingAudio) {
                        Modifier
                            .size(dimensionResource(id = R.dimen.call_screen_voicevox_icon_size))
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                            .border(
                                width = dimensionResource(id = R.dimen.circle_indicator_width),
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    } else {
                        Modifier
                            .size(dimensionResource(id = R.dimen.call_screen_voicevox_icon_size))
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                    }
                )
            }
            Text(
                text = viewModel.currentVoicevox.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.medium_padding)),
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    toPreviousScreen()
                    viewModel.stopAudio()
                    viewModel.stopSpeechRecognizer()
                },
                modifier = Modifier.size(dimensionResource(id = R.dimen.extra_large_icon_size)),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                )
            }
        }
    }
}