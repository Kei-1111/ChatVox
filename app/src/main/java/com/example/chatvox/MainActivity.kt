package com.example.chatvox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.chatvox.ui.component.ChatVoxDialog
import com.example.chatvox.ui.navigation.ChatVoxNavigation
import com.example.chatvox.ui.navigation.Screens
import com.example.chatvox.ui.theme.ChatVoxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val appSettings by viewModel.appSettings.collectAsState()

            splashScreen.setKeepOnScreenCondition {
                appSettings.isLoading
            }

            val navController = rememberNavController()

            intent.getStringExtra(Screens.KEY_DESTINATION)?.takeIf { it.isNotEmpty() }?.let {
                LaunchedEffect(it) {
                    navController.navigate(it)
                }
            }

            ChatVoxTheme(
                darkTheme = if (appSettings.isFollowSystemTheme) isSystemInDarkTheme() else appSettings.isDarkTheme,
                dynamicColor = appSettings.isDynamicColor,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ChatVoxNavigation(
                        modifier = Modifier
                            .fillMaxSize(),
                        navController = navController
                    )
                }
                if(appSettings.userName.isBlank() || appSettings.userName.isEmpty()) {
                    ChatVoxDialog(
                        oldUserName = appSettings.userName,
                        onRegister = viewModel::setUserName
                    )
                }
            }
        }
    }
}





