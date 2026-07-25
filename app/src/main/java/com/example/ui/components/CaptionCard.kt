package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.model.AppLanguage
import com.example.model.GeneratedCaption
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.NeonViolet

@Composable
fun CaptionCard(
    caption: GeneratedCaption,
    isSaved: Boolean,
    onCopy: (String) -> Unit,
    onShare: (String) -> Unit,
    onSave: (GeneratedCaption) -> Unit,
    modifier: Modifier = Modifier
) {
    var isBookmarked by remember { mutableStateOf(isSaved) }
    var showBreakdown by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    val isArabic = caption.language == AppLanguage.ARABIC
    val fullText = caption.getFullText()

    if (showExportDialog) {
        ExportFormatDialog(
            caption = caption,
            isArabic = isArabic,
            onDismiss = { showExportDialog = false },
            onCopyFormatted = { onCopy(it) },
            onShareFormatted = { onShare(it) }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("caption_card_${caption.id}")
            .clip(RoundedCornerShape(22.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(22.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Platform Badge + Viral Score (Clickable for breakdown)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Platform Tag
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = caption.platform.brandColor.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(caption.platform.brandColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = caption.platform.displayName,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = caption.platform.brandColor
                        )
                    }
                }

                // Estimated Viral Score Badge - Interactive Breakdown Toggle
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { showBreakdown = !showBreakdown },
                    shape = RoundedCornerShape(12.dp),
                    color = EmeraldSuccess.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ " + (if (isArabic) "نسبة الفيرال: " else "Viral Score: ") + "${caption.estimatedViralScore}%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (showBreakdown) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Breakdown",
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Quality & Viral Score Breakdown Panel
            AnimatedVisibility(
                visible = showBreakdown,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Surface(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (isArabic) "📊 تحليل قوة وانتشار الكابشن:" else "📊 Caption Quality & Viral Breakdown:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Hook Power Progress
                        ScoreProgressBar(
                            label = if (isArabic) "قوة الخطاف (Hook Power)" else "Hook Power",
                            score = caption.hookScore,
                            barColor = NeonViolet
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // Engagement Score Progress
                        ScoreProgressBar(
                            label = if (isArabic) "معدل التفاعل المتوقع" else "Expected Engagement",
                            score = caption.engagementScore,
                            barColor = ElectricIndigo
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // SEO Hashtags Progress
                        ScoreProgressBar(
                            label = if (isArabic) "توافق محركات البحث وSEO" else "SEO & Hashtag Rating",
                            score = caption.seoScore,
                            barColor = EmeraldSuccess
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quality Improvement Tips
                        caption.qualityTips.forEach { tip ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("✨", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = tip,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Caption Content
            if (caption.hook.isNotBlank()) {
                Text(
                    text = caption.hook,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (caption.body.isNotBlank()) {
                Text(
                    text = caption.body,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!caption.callToAction.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = caption.callToAction,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            // Hashtags
            if (caption.hashtags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = caption.hashtags.joinToString(" ") { if (it.startsWith("#")) it else "#$it" },
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // Footer Action Buttons: Export/Format, Copy, Share, Save
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Style tag label
                Text(
                    text = "${caption.style.iconSymbol} ${caption.style.getDisplayName(caption.language)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Export Preset Button
                    IconButton(
                        onClick = { showExportDialog = true },
                        modifier = Modifier
                            .testTag("export_button_${caption.id}")
                            .size(38.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.IosShare,
                            contentDescription = "Export Formatted",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Copy Button
                    IconButton(
                        onClick = { onCopy(fullText) },
                        modifier = Modifier
                            .testTag("copy_button_${caption.id}")
                            .size(38.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = "Copy Caption",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Share Button
                    IconButton(
                        onClick = { onShare(fullText) },
                        modifier = Modifier
                            .testTag("share_button_${caption.id}")
                            .size(38.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share Caption",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Save / Bookmark Button
                    IconButton(
                        onClick = {
                            isBookmarked = !isBookmarked
                            onSave(caption)
                        },
                        modifier = Modifier
                            .testTag("save_button_${caption.id}")
                            .size(38.dp)
                            .background(
                                color = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Save Caption",
                            tint = if (isBookmarked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreProgressBar(
    label: String,
    score: Int,
    barColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$score%",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                color = barColor
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        LinearProgressIndicator(
            progress = score / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(CircleShape),
            color = barColor,
            trackColor = barColor.copy(alpha = 0.2f)
        )
    }
}

