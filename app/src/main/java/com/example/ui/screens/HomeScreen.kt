package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.model.AppLanguage
import com.example.model.SocialPlatform
import com.example.ui.components.CaptionCard
import com.example.ui.navigation.NavRoutes
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.RosePink
import com.example.ui.viewmodel.CaptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: CaptionViewModel
) {
    val context = LocalContext.current
    val options by viewModel.options.collectAsState()
    val savedCaptions by viewModel.savedCaptions.collectAsState()
    val isArabic = options.language == AppLanguage.ARABIC

    val quickStarters = if (isArabic) listOf(
        "روتين الصباح وإنجاز المهام 🌅",
        "إطلاق منتج جديد أو خدمة 🚀",
        "لحظة سفر واسترخاء غروب ✈️",
        "تحدي رياضي ولياقة جيمة 💪",
        "نصيحة سريعة لتطوير الذات 💡"
    ) else listOf(
        "Morning Routine & Productivity 🌅",
        "New Product or Service Launch 🚀",
        "Sunset Travel Vibe ✈️",
        "Gym & Fitness Motivation 💪",
        "Quick Self-Growth Tip 💡"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = NeonViolet
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("✨", fontSize = 18.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Caption AI",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                },
                actions = {
                    val isProUser by viewModel.isProUser.collectAsState()
                    // Pro Badge Button
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.showProPaywallDialog() }
                            .testTag("pro_badge_topbar"),
                        color = if (isProUser) NeonViolet.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isProUser) "👑 PRO" else "⭐ UPGRADE",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isProUser) NeonViolet else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Language Switcher Button
                    TextButton(
                        onClick = {
                            val nextLang = if (isArabic) AppLanguage.ENGLISH else AppLanguage.ARABIC
                            viewModel.updateLanguage(nextLang)
                        },
                        modifier = Modifier.testTag("language_toggle_button")
                    ) {
                        Text(
                            text = "${options.language.flagEmoji} ${options.language.nativeName}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    // Settings Button
                    IconButton(
                        onClick = { navController.navigate(NavRoutes.Settings.route) },
                        modifier = Modifier.testTag("settings_nav_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Hero Banner Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(26.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(NeonViolet, ElectricIndigo)
                            )
                        )
                        .clickable { navController.navigate(NavRoutes.Generator.route) }
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = if (isArabic) "🔥 الذكاء الاصطناعي الفائق" else "🔥 Viral AI Generator",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White
                                )
                            }
                            Icon(
                                imageVector = Icons.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = if (isArabic) "أنشئ كابشن احترافي في ثوانٍ" else "Generate High-Converting Social Captions",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isArabic) "اختر المنصة، الأسلوب، والمود ليقوم الذكاء الاصطناعي بكتابة النص كاملاً"
                            else "Select platform, style, tone & length. Perfect for Instagram, TikTok & Shorts.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { navController.navigate(NavRoutes.Generator.route) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = ElectricIndigo
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("start_generator_hero_button")
                        ) {
                            Text(
                                text = if (isArabic) "ابدأ التوليد الآن ✨" else "Start Generating Now ✨",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            // Quick Actions Row: Image Upload & Hashtags
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Vision Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { navController.navigate(NavRoutes.ImageUpload.route) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(RosePink.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AddPhotoAlternate,
                                    contentDescription = null,
                                    tint = RosePink
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (isArabic) "كابشن الصور" else "Image Vision AI",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isArabic) "ارفع صورة ليحللها AI" else "Upload photo & analyze",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Hashtags Generator Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { navController.navigate(NavRoutes.Hashtags.route) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(NeonViolet.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Tag,
                                    contentDescription = null,
                                    tint = NeonViolet
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (isArabic) "مولد الهاشتاج" else "Hashtag Hub",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isArabic) "هاشتاجات شائعة ومخصصة" else "Find viral & niche tags",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Quick Prompt Starters Section
            item {
                Column {
                    Text(
                        text = if (isArabic) "💡 أفكار سريعة للبدء" else "💡 Quick Idea Starters",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(quickStarters) { idea ->
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable {
                                        viewModel.updateTopic(idea)
                                        navController.navigate(NavRoutes.Generator.route)
                                    },
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Text(
                                    text = idea,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                )
                            }
                        }
                    }
                }
            }

            // Platforms Showcase Row
            item {
                Column {
                    Text(
                        text = if (isArabic) "📲 المنصات المدعومة" else "📲 Supported Platforms",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(SocialPlatform.entries) { platform ->
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable {
                                        viewModel.updatePlatform(platform)
                                        navController.navigate(NavRoutes.Generator.route)
                                    },
                                color = platform.brandColor.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(platform.brandColor)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = platform.displayName,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = platform.brandColor
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Recent Saved Captions Preview
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isArabic) "📌 آخر المحفوظات" else "📌 Recent Saved Captions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    if (savedCaptions.isNotEmpty()) {
                        TextButton(onClick = { navController.navigate(NavRoutes.Saved.route) }) {
                            Text(if (isArabic) "عرض الكل" else "View All")
                        }
                    }
                }
            }

            if (savedCaptions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📌", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isArabic) "لا توجد كابشنات محفوظة بعد" else "No saved captions yet",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                            )
                            Text(
                                text = if (isArabic) "مولّد الكابشن واحفظ الأفضل لمشاركتك" else "Generate captions and save your favorites here",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(savedCaptions.take(2)) { entity ->
                    val platform = SocialPlatform.fromId(entity.platform)
                    val dummyCaption = com.example.model.GeneratedCaption(
                        id = entity.id.toString(),
                        hook = entity.hook,
                        body = entity.fullText.removePrefix(entity.hook).trim(),
                        platform = platform,
                        language = AppLanguage.fromCode(entity.language),
                        isSaved = true
                    )
                    CaptionCard(
                        caption = dummyCaption,
                        isSaved = true,
                        onCopy = { text -> viewModel.copyToClipboard(context, text) },
                        onShare = { text -> viewModel.shareCaption(context, text) },
                        onSave = { viewModel.deleteSavedCaption(entity.id, context) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
