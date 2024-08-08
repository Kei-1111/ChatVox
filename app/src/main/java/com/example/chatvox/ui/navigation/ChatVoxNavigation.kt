package com.example.chatvox.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.chatvox.ui.navigation.Screens.Call
import com.example.chatvox.ui.navigation.Screens.Chat
import com.example.chatvox.ui.navigation.Screens.Home
import com.example.chatvox.ui.navigation.Screens.Settings
import com.example.chatvox.ui.screen.call.CallScreen
import com.example.chatvox.ui.screen.chat.ChatScreen
import com.example.chatvox.ui.screen.home.HomeScreen
import com.example.chatvox.ui.screen.settings.SettingsScreen

@Composable
fun ChatVoxNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        startDestination = Home.route,
        navController = navController,
    ){
        composable(route = Home.route){
            HomeScreen(
                toChatScreen = {
                    navController.navigate("${Chat.route}/${it.name}")
                },
                toSettingsScreen = {
                    navController.navigate(Settings.route)
                }
            )
        }

        composable(
            route = "${Chat.route}/{voicevoxType}",
            arguments = listOf(navArgument("voicevoxType") { type = NavType.StringType })
        ){
            ChatScreen(
                toHomeScreen = {
                    navController.navigate(Home.route)
                },
                toCallScreen = {
                    navController.navigate("${Call.route}/${it.name}")
                },
            )
        }

        composable(route = Settings.route){
             SettingsScreen(
                 toPreviousScreen = {
                     navController.popBackStack()
                 }
             )
        }

        composable(
            route = "${Call.route}/{voicevoxType}",
            arguments = listOf(navArgument("voicevoxType") { type = NavType.StringType })
        ){
            CallScreen(
                toPreviousScreen = {
                    navController.popBackStack()
                }
            )
        }
    }
}