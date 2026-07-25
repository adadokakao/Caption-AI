package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.model.AppLanguage
import com.example.model.SocialPlatform
import com.example.ui.components.CaptionCard
import com.example.ui.components.SelectableChip
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.RosePink
import com.example.ui.viewmodel.CaptionViewModel
import com.example.ui.viewmodel.GenerationUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCaptionScreen(
    navController: NavController,
    viewModel: CaptionViewModel
) {
    val context = LocalContext.current
    val options by viewModel.options.collectAsState()
    val uiState by viewModel.generationState.collectAsState()
    val selectedBitmap by viewModel.selectedBitmap.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()

    val isArabic = options.language == AppLanguage.ARABIC

    // Gallery Picker Launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.setImageFromUri(context, it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isArabic) "📷 كابشن بالذكاء الاصطناعي للصور" else "📷 Image Vision AI Caption",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.testTag("back_button_image_screen")
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

            // Image Upload Box / Preview
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .clickable { launcher.launch("image/*") }
                        .testTag("image_upload_box"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedBitmap != null) {
                            Image(
                                bitmap = selectedBitmap!!.asImageBitmap(),
                                contentDescription = "Uploaded Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            // Remove image overlay button
                            IconButton(
                                onClick = { viewModel.clearImage() },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White)
                            }
                        } else if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Uploaded Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { viewModel.clearImage() },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White)
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AddPhotoAlternate,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = if (isArabic) "اضغط هنا لرفع صورة من الاستوديو" else "Tap here to select photo from Gallery",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isArabic) "سيقوم الذكاء الاصطناعي بتحليل الألوان والعناصر والأجواء"
                                    else "AI will analyze elements, mood, color palette & scene",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Quick Sample Photo Buttons (for instant testing without needing real device photos)
            item {
                Column {
                    Text(
                        text = if (isArabic) "🎨 أو اختر عينة تجريبية للتحليل" else "🎨 Or try a quick sample photo",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = false,
                                onClick = {
                                    val bitmap = createSampleBitmap("🌅 Sunset Beach")
                                    viewModel.setImageBitmap(bitmap)
                                    viewModel.updateTopic(if (isArabic) "غروب الشمس على شاطئ البحر" else "Sunset over ocean beach")
                                },
                                label = { Text("🌅 Beach Sunset") }
                            )
                        }
                        item {
                            FilterChip(
                                selected = false,
                                onClick = {
                                    val bitmap = createSampleBitmap("☕ Espresso Coffee")
                                    viewModel.setImageBitmap(bitmap)
                                    viewModel.updateTopic(if (isArabic) "قهوة الصباح واستراحة عمل" else "Morning coffee breakdown")
                                },
                                label = { Text("☕ Coffee Spot") }
                            )
                        }
                        item {
                            FilterChip(
                                selected = false,
                                onClick = {
                                    val bitmap = createSampleBitmap("🏎️ Luxury Sports Car")
                                    viewModel.setImageBitmap(bitmap)
                                    viewModel.updateTopic(if (isArabic) "سيارة رياضية فاخرة وسفر" else "Luxury supercar drive")
                                },
                                label = { Text("🏎️ Supercar Drive") }
                            )
                        }
                    }
                }
            }

            // Target Platform Choice
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

            // Generate Button
            item {
                Button(
                    onClick = { viewModel.generateCaptions() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("generate_image_captions_button"),
                    shape = RoundedCornerShape(18.dp),
                    enabled = uiState !is GenerationUiState.Loading && (selectedBitmap != null || options.topicOrIdea.isNotBlank()),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (uiState is GenerationUiState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(if (isArabic) "جاري تحليل الصورة والتوليد..." else "Analyzing image & generating...")
                    } else {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isArabic) "توليد كابشن الصورة بالذكاء الاصطناعي 🚀" else "Generate Image Captions 🚀",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // Results Section
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
                        Text(
                            text = if (isArabic) "🎉 نتائـج تحليل الصورة (${state.captions.size})" else "🎉 Image Vision Captions (${state.captions.size})",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                        )
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

// Helper function to generate a placeholder Bitmap for instant sample testing
private fun createSampleBitmap(label: String): Bitmap {
    val bitmap = Bitmap.createBitmap(400, 300, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply {
        color = android.graphics.Color.parseColor("#312E81")
    }
    canvas.drawRect(0f, 0f, 400f, 300f, paint)

    val textPaint = Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 32f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText(label, 200f, 160f, textPaint)
    return bitmap
}
