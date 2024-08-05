package com.example.chatvox.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.example.chatvox.ui.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatVoxTopAppBar(
    title: String,
    currentScreen: Screens,
    back: () -> Unit = {},
    action: () -> Unit = {},
    actionEnabled: Boolean = true,
) {
    TopAppBar(
        title = {
            when (currentScreen) {
                Screens.Home -> {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                }

                else -> {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
        },
        navigationIcon = {
            when (currentScreen) {
                Screens.Home -> {}

                Screens.Chat -> {
                    IconButton(
                        onClick = back
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

                Screens.Settings -> {
                    IconButton(
                        onClick = back
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

                Screens.Call -> {}
            }
        },
        actions = {
            when (currentScreen) {
                Screens.Home -> {
                    IconButton(
                        onClick = action
                    ){
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }

                Screens.Chat -> {
                    IconButton(
                        onClick = action,
                        enabled = actionEnabled
                    ){
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call"
                        )
                    }
                }

                Screens.Settings -> {}

                Screens.Call -> {}
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary,
        ),
    )
}