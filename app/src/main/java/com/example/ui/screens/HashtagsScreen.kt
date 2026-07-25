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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.data.repository.HashtagCategory
import com.example.data.repository.HashtagRepository
import com.example.model.AppLanguage
import com.example.ui.viewmodel.CaptionViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HashtagsScreen(
    navController: NavController,
    viewModel: CaptionViewModel
) {
    val context = LocalContext.current
    val options by viewModel.options.collectAsState()
    val isArabic = options.language == AppLanguage.ARABIC

    val hashtagRepo = remember { HashtagRepository() }
    var selectedCategory by remember { mutableStateOf(hashtagRepo.categories.first()) }
    var customKeyword by remember { mutableStateOf("") }
    var generatedTags by remember { mutableStateOf(selectedCategory.tags) }

    LaunchedEffect(selectedCategory) {
        if (customKeyword.isBlank()) {
            generatedTags = selectedCategory.tags
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isArabic) "#️⃣ مولد الهاشتاجات الذكي" else "#️⃣ Hashtag Generator & Hub",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.testTag("back_button_hashtags")
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

            // Custom Keyword Hashtag Generator Box
            item {
                Column {
                    Text(
                        text = if (isArabic) "🔍 توليد هاشتاجات حسب النيش / الموضوع" else "🔍 Generate Hashtags by Niche or Keyword",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = customKeyword,
                            onValueChange = { customKeyword = it },
                            placeholder = { Text(if (isArabic) "ادخل كلمة مثل: قهوة، سفر، الذكاء الاصطناعي..." else "Enter keyword e.g. fitness, ai, travel...") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("hashtag_keyword_input"),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                if (customKeyword.isNotBlank()) {
                                    generatedTags = hashtagRepo.generateHashtagsForTopic(customKeyword, options.language)
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("generate_hashtags_button")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        }
                    }
                }
            }

            // Categories Selector Row
            item {
                Column {
                    Text(
                        text = if (isArabic) "📂 تصنيفات الهاشتاجات الشائعة" else "📂 Categorized Trending Hashtags",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(hashtagRepo.categories) { cat ->
                            val isSelected = selectedCategory.id == cat.id && customKeyword.isBlank()
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        customKeyword = ""
                                        selectedCategory = cat
                                        generatedTags = cat.tags
                                    },
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = if (isArabic) cat.nameAr else cat.nameEn,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Hashtags Display Section & One-Tap Copy All
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isArabic) "الهاشتاجات المقترحة (${generatedTags.size})" else "Suggested Hashtags (${generatedTags.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )

                            Button(
                                onClick = {
                                    val allStr = generatedTags.joinToString(" ")
                                    viewModel.copyToClipboard(context, allStr)
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("copy_all_hashtags_button")
                            ) {
                                Icon(Icons.Outlined.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isArabic) "نسخ الكل" else "Copy All")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // FlowRow of Hashtag Chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            generatedTags.forEach { tag ->
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            viewModel.copyToClipboard(context, tag)
                                        },
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = tag,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
