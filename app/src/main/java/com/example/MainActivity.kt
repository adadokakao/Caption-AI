package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.model.AppLanguage
import com.example.ui.components.BottomNavBar
import com.example.ui.components.ProPaywallDialog
import com.example.ui.navigation.NavRoutes
import com.example.ui.screens.*
import com.example.ui.theme.CaptionAITheme
import com.example.ui.viewmodel.CaptionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CaptionAITheme {
                val navController = rememberNavController()
                val viewModel: CaptionViewModel = viewModel()
                val options by viewModel.options.collectAsState()
                val isArabic = options.language == AppLanguage.ARABIC
                val layoutDirection = if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr

                val isProUser by viewModel.isProUser.collectAsState()
                val showProPaywall by viewModel.showProPaywall.collectAsState()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = currentRoute in listOf(
                    NavRoutes.Home.route,
                    NavRoutes.Generator.route,
                    NavRoutes.ImageUpload.route,
                    NavRoutes.Hashtags.route,
                    NavRoutes.Saved.route
                )

                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    if (showProPaywall) {
                        ProPaywallDialog(
                            isArabic = isArabic,
                            isCurrentlyPro = isProUser,
                            onDismiss = { viewModel.dismissProPaywallDialog() },
                            onTogglePro = { viewModel.toggleProStatus(it) }
                        )
                    }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (showBottomBar) {
                                BottomNavBar(
                                    navController = navController,
                                    isArabic = isArabic
                                )
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = NavRoutes.Splash.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(NavRoutes.Splash.route) {
                                SplashScreen(navController = navController)
                            }
                            composable(NavRoutes.Home.route) {
                                HomeScreen(navController = navController, viewModel = viewModel)
                            }
                            composable(NavRoutes.Generator.route) {
                                CaptionGeneratorScreen(navController = navController, viewModel = viewModel)
                            }
                            composable(NavRoutes.ImageUpload.route) {
                                ImageCaptionScreen(navController = navController, viewModel = viewModel)
                            }
                            composable(NavRoutes.Hashtags.route) {
                                HashtagsScreen(navController = navController, viewModel = viewModel)
                            }
                            composable(NavRoutes.Saved.route) {
                                SavedCaptionsScreen(navController = navController, viewModel = viewModel)
                            }
                            composable(NavRoutes.Settings.route) {
                                SettingsScreen(navController = navController, viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

