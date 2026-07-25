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
import com.example.model.*
import com.example.ui.components.CaptionCard
import com.example.ui.components.SelectableChip
import com.example.ui.theme.NeonViolet
import com.example.ui.viewmodel.CaptionViewModel
import com.example.ui.viewmodel.GenerationUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptionGeneratorScreen(
    navController: NavController,
    viewModel: CaptionViewModel
) {
    val context = LocalContext.current
    val options by viewModel.options.collectAsState()
    val uiState by viewModel.generationState.collectAsState()
    val isArabic = options.language == AppLanguage.ARABIC

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isArabic) "✨ مولد الكابشن الذكي" else "✨ AI Caption Generator",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.testTag("back_button_generator")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Language switcher chip
                    TextButton(
                        onClick = {
                            val nextLang = if (isArabic) AppLanguage.ENGLISH else AppLanguage.ARABIC
                            viewModel.updateLanguage(nextLang)
                        }
                    ) {
                        Text("${options.language.flagEmoji} ${options.language.nativeName}")
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

            // 1. Topic Input Box
            item {
                Column {
                    Text(
                        text = if (isArabic) "📝 فكرة الكابشن أو موضوع الصورة" else "📝 What is your content about?",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = options.topicOrIdea,
                        onValueChange = { viewModel.updateTopic(it) },
                        placeholder = {
                            Text(
                                text = if (isArabic) "مثال: صور رحلة الصحراء والغروب مع الأصدقاء..."
                                else "e.g. Sunset vibes at the beach with close friends..."
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("topic_input_field"),
                        shape = RoundedCornerShape(18.dp),
                        trailingIcon = {
                            if (options.topicOrIdea.isNotEmpty()) {
                                IconButton(onClick = { viewModel.updateTopic("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                    )
                }
            }

            // 2. Social Platform Selector
            item {
                Column {
                    Text(
                        text = if (isArabic) "📱 اختر المنصة Target Platform" else "📱 Target Platform",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(SocialPlatform.entries) { platform ->
                            SelectableChip(
                                text = platform.displayName,
                                isSelected = options.platform == platform,
                                onSelect = { viewModel.updatePlatform(platform) },
                                brandColor = platform.brandColor
                            )
                        }
                    }
                }
            }

            // 3. Caption Style Selector
            item {
                Column {
                    Text(
                        text = if (isArabic) "🎨 أسلوب الكابشن Style" else "🎨 Caption Style",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(CaptionStyle.entries) { style ->
                            SelectableChip(
                                text = style.getDisplayName(options.language),
                                isSelected = options.style == style,
                                onSelect = { viewModel.updateStyle(style) },
                                icon = style.iconSymbol
                            )
                        }
                    }
                }
            }

            // 4. Tone Selector
            item {
                Column {
                    Text(
                        text = if (isArabic) "🎭 نبرة الصوت Tone" else "🎭 Tone of Voice",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(CaptionTone.entries) { tone ->
                            SelectableChip(
                                text = tone.getDisplayName(options.language),
                                isSelected = options.tone == tone,
                                onSelect = { viewModel.updateTone(tone) },
                                icon = tone.emoji
                            )
                        }
                    }
                }
            }

            // 5. Length Selector
            item {
                Column {
                    Text(
                        text = if (isArabic) "📏 طول الكابشن Length" else "📏 Caption Length",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CaptionLength.entries.forEach { len ->
                            Box(modifier = Modifier.weight(1f)) {
                                SelectableChip(
                                    text = len.getDisplayName(options.language).substringBefore(" "),
                                    isSelected = options.length == len,
                                    onSelect = { viewModel.updateLength(len) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }

            // 6. Toggles (Emojis, Hashtags, Call to Action)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(if (isArabic) "إضافة إيموجي Emojis 😊" else "Include Emojis 😊")
                            Switch(
                                checked = options.includeEmojis,
                                onCheckedChange = { viewModel.toggleEmojis(it) }
                            )
                        }
                        Divider(modifier = Modifier.padding(vertical = 6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(if (isArabic) "توليد هاشتاجات Hashtags #️⃣" else "Include Hashtags #️⃣")
                            Switch(
                                checked = options.includeHashtags,
                                onCheckedChange = { viewModel.toggleHashtags(it) }
                            )
                        }
                        Divider(modifier = Modifier.padding(vertical = 6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(if (isArabic) "دعوة للتفاعل CTA 💬" else "Include Call to Action 💬")
                            Switch(
                                checked = options.includeCta,
                                onCheckedChange = { viewModel.toggleCta(it) }
                            )
                        }
                    }
                }
            }

            // 7. Generate Button
            item {
                Button(
                    onClick = { viewModel.generateCaptions() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("generate_captions_button"),
                    shape = RoundedCornerShape(18.dp),
                    enabled = uiState !is GenerationUiState.Loading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (uiState is GenerationUiState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(if (isArabic) "جاري توليد الكابشن..." else "Generating Captions...")
                    } else {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isArabic) "توليد الكابشن بالذكاء الاصطناعي ✨" else "Generate AI Captions ✨",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // 8. Results Display Section
            when (val state = uiState) {
                is GenerationUiState.Error -> {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
                is GenerationUiState.Success -> {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isArabic) "🎉 النتائج المقترحة (${state.captions.size})" else "🎉 AI Caption Suggestions (${state.captions.size})",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                            )
                            IconButton(onClick = { viewModel.generateCaptions() }) {
                                Icon(Icons.Filled.Refresh, contentDescription = "Regenerate")
                            }
                        }
                    }

                    items(state.captions) { generated ->
                        CaptionCard(
                            caption = generated,
                            isSaved = false,
                            onCopy = { text -> viewModel.copyToClipboard(context, text) },
                            onShare = { text -> viewModel.shareCaption(context, text) },
                            onSave = { caption -> viewModel.saveCaption(caption, context) }
                        )
                    }
                }
                else -> {}
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
