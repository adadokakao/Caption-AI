package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.model.AppLanguage
import com.example.model.GeneratedCaption
import com.example.model.SocialPlatform
import com.example.ui.components.CaptionCard
import com.example.ui.components.SelectableChip
import com.example.ui.viewmodel.CaptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedCaptionsScreen(
    navController: NavController,
    viewModel: CaptionViewModel
) {
    val context = LocalContext.current
    val options by viewModel.options.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedPlatformFilter by viewModel.selectedSavedPlatform.collectAsState()
    val savedCaptions by viewModel.savedCaptions.collectAsState()

    val isArabic = options.language == AppLanguage.ARABIC

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isArabic) "📌 الكابشنات المحفوظة" else "📌 Saved Favorite Captions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.testTag("back_button_saved")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(2.dp)) }

            // Search Bar Input
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text(if (isArabic) "بحث في المحفوظات..." else "Search saved captions...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("saved_search_input"),
                    shape = RoundedCornerShape(18.dp),
                    singleLine = true
                )
            }

            // Platform Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        SelectableChip(
                            text = if (isArabic) "الكل" else "All Platforms",
                            isSelected = selectedPlatformFilter == null,
                            onSelect = { viewModel.setSavedPlatformFilter(null) }
                        )
                    }
                    items(SocialPlatform.entries) { platform ->
                        SelectableChip(
                            text = platform.displayName,
                            isSelected = selectedPlatformFilter == platform,
                            onSelect = { viewModel.setSavedPlatformFilter(platform) },
                            brandColor = platform.brandColor
                        )
                    }
                }
            }

            // Saved List
            if (savedCaptions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📁", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (isArabic) "لا توجد نتائج محفوظة" else "No saved captions found",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isArabic) "قم بتوليد كابشن واضغط على زر الحفظ للرجوع إليه لاحقاً"
                                else "Generate captions & tap the save icon to store them here offline.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(savedCaptions, key = { it.id }) { entity ->
                    val platform = SocialPlatform.fromId(entity.platform)
                    val caption = GeneratedCaption(
                        id = entity.id.toString(),
                        hook = entity.hook,
                        body = entity.fullText.removePrefix(entity.hook).trim(),
                        platform = platform,
                        language = AppLanguage.fromCode(entity.language),
                        isSaved = true
                    )

                    CaptionCard(
                        caption = caption,
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
