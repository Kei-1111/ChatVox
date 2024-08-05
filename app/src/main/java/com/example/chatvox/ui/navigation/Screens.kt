package com.example.chatvox.ui.navigation

sealed class Screens(
    val route: String
){
    data object Home: Screens("home")
    data object Chat: Screens("chat")
    data object Settings: Screens("settings")
    data object Call: Screens("call")
}