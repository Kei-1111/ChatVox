package com.example.chatvox.model

data class AppSettings(
    val isDynamicColor: Boolean = false,
    val isFollowSystemTheme: Boolean = true,
    val isDarkTheme: Boolean = false,
    val userName: String = "",
    val isLoading: Boolean = false
)
