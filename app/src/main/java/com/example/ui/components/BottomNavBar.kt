package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ui.navigation.NavRoutes
import com.example.ui.theme.NeonViolet

data class BottomNavItem(
    val route: String,
    val labelEn: String,
    val labelAr: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
)

@Composable
fun BottomNavBar(
    navController: NavController,
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem(NavRoutes.Home.route, "Home", "الرئيسية", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem(NavRoutes.Generator.route, "AI Generator", "المولد", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
        BottomNavItem(NavRoutes.ImageUpload.route, "Vision", "الصور", Icons.Filled.AddPhotoAlternate, Icons.Outlined.AddPhotoAlternate),
        BottomNavItem(NavRoutes.Hashtags.route, "Hashtags", "الهاشتاجات", Icons.Filled.Tag, Icons.Outlined.Tag),
        BottomNavItem(NavRoutes.Saved.route, "Saved", "المحفوظات", Icons.Filled.Bookmark, Icons.Outlined.Bookmark)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 8.dp,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                val label = if (isArabic) item.labelAr else item.labelEn

                val iconColor by animateColorAsState(
                    targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    label = "iconColor"
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(NavRoutes.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        .background(
                            if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            else Color.Transparent
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (selected) item.activeIcon else item.inactiveIcon,
                            contentDescription = label,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = iconColor
                        )
                    }
                }
            }
        }
    }
}
