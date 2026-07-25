package com.example.ui.navigation

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Home : NavRoutes("home")
    object Generator : NavRoutes("generator")
    object ImageUpload : NavRoutes("image_upload")
    object Hashtags : NavRoutes("hashtags")
    object Saved : NavRoutes("saved")
    object Settings : NavRoutes("settings")
}
