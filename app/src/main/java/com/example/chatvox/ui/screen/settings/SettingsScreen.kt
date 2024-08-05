package com.example.chatvox.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatvox.R
import com.example.chatvox.ui.component.ChatVoxDialog
import com.example.chatvox.ui.component.ChatVoxSettingSwitch
import com.example.chatvox.ui.component.ChatVoxTopAppBar
import com.example.chatvox.ui.navigation.Screens

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    toPreviousScreen: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ChatVoxTopAppBar(
                title = "設定",
                currentScreen = Screens.Settings,
                back = toPreviousScreen
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    horizontal = dimensionResource(id = R.dimen.medium_padding),
                    vertical = dimensionResource(id = R.dimen.medium_padding)
                ),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.small_padding)
            )
        ) {
            SettingTheme(
                isDynamicColor = uiState.isDynamicColor,
                onDynamicColorCheckedChange = viewModel::onDynamicColorCheckedChange,
                isFollowSystemTheme = uiState.isFollowSystemTheme,
                onFollowSystemThemeCheckedChange = viewModel::onFollowSystemThemeCheckedChange,
                isDarkTheme = uiState.isDarkTheme,
                onDarkThemeCheckedChange = viewModel::onDarkThemeCheckedChange
            )
            SettingUser(
                userName = uiState.userName,
                setUserName = viewModel::setUserName
            )
        }
    }
}

@Composable
private fun SettingTheme(
    isDynamicColor: Boolean,
    onDynamicColorCheckedChange: (Boolean) -> Unit,
    isFollowSystemTheme: Boolean,
    onFollowSystemThemeCheckedChange: (Boolean) -> Unit,
    isDarkTheme: Boolean,
    onDarkThemeCheckedChange: (Boolean) -> Unit
) {
    var isShow by remember { mutableStateOf(false) }

    Surface(
        shape = MaterialTheme.shapes.small,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.extra_small_padding)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { isShow = !isShow }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(id = R.dimen.medium_padding)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = "テーマ",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.medium_icon_size)),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "テーマ",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                if (isShow) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "展開",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "展開",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            if (isShow) {
                Column(
                    modifier = Modifier
                        .padding(
                            vertical = dimensionResource(id = R.dimen.extra_small_padding),
                            horizontal = dimensionResource(id = R.dimen.medium_padding),
                        ),
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(id = R.dimen.small_padding)
                    )
                ) {
                    ChatVoxSettingSwitch(
                        description = "ダイナミックカラーを使用しますか？",
                        checked = isDynamicColor,
                        onCheckedChange = onDynamicColorCheckedChange
                    )
                    ChatVoxSettingSwitch(
                        description = "システムのテーマに従いますか？",
                        checked = isFollowSystemTheme,
                        onCheckedChange = onFollowSystemThemeCheckedChange
                    )
                    ChatVoxSettingSwitch(
                        description = "ダークモードにしますか？",
                        checked = isDarkTheme,
                        onCheckedChange = onDarkThemeCheckedChange,
                        enabled = !isFollowSystemTheme
                    )
                }
            }
        }
    }
}


@Composable
private fun SettingUser(
    userName: String,
    setUserName: (String) -> Unit
) {
    var isShow by remember { mutableStateOf(false) }

    Surface(
        shape = MaterialTheme.shapes.small,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.extra_small_padding)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { isShow = !isShow }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(id = R.dimen.medium_padding)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "ユーザー",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.medium_icon_size)),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "ユーザー",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                if (isShow) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "展開",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "展開",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
    if (isShow) {
        ChatVoxDialog(
            oldUserName = userName,
            onRegister = {
                setUserName(it)
                isShow = false
            },
            onDismissRequest = { isShow = false }
        )
    }
}