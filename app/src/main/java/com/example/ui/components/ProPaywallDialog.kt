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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.RosePink

@Composable
fun ProPaywallDialog(
    isArabic: Boolean,
    isCurrentlyPro: Boolean,
    onDismiss: () -> Unit,
    onTogglePro: (Boolean) -> Unit
) {
    var selectedPlan by remember { mutableStateOf("annual") } // "annual" or "monthly"

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(listOf(NeonViolet, RosePink)),
                    shape = RoundedCornerShape(28.dp)
                )
                .testTag("pro_paywall_dialog"),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Close Button
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(18.dp))
                    }
                }

                // Pro Badge Icon Header
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(listOf(NeonViolet, ElectricIndigo))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.WorkspacePremium,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isArabic) "Caption AI PRO 👑" else "Caption AI PRO 👑",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isArabic) "اطلاق العنان لقوة الذكاء الاصطناعي الكاملة لصناعة المحتوى"
                    else "Unlock the full power of Gemini AI for viral content growth",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Feature List
                val features = if (isArabic) listOf(
                    "🚀 توليد كابشنات بلا حدود مع Gemini 3.5 Flash",
                    "👁️ تحليل الصور المتقدم برؤية الذكاء الاصطناعي",
                    "⚡ تحليل الفيرال وتقييم قوة الخطاف وSEO",
                    "📋 تصدير متعدد المنسقات (انستجرام، تيك توك، لينكد إن، JSON)",
                    "🇸🇦 دعم اللهجات العربية والترجمة الثقافية"
                ) else listOf(
                    "🚀 Unlimited Gemini 3.5 AI Caption Generations",
                    "👁️ Deep Image Vision Scene & Object Analysis",
                    "⚡ Viral Potential Breakdown (Hook, SEO & Quality)",
                    "📋 Multi-Platform Auto Formatters (IG, TikTok, LinkedIn)",
                    "🇸🇦 Arabic Dialect Customization & Localized Hooks"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    features.forEach { feature ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldSuccess,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = feature,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Plan Selectors
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Annual Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = if (selectedPlan == "annual") 2.dp else 1.dp,
                                color = if (selectedPlan == "annual") NeonViolet else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable { selectedPlan = "annual" },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedPlan == "annual") NeonViolet.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isArabic) "سنوي (وفر 50%)" else "Annual (Save 50%)",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = NeonViolet
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$4.99/mo",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = if (isArabic) "$59.99 سنوياً" else "Billed $59.99/yr",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Monthly Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = if (selectedPlan == "monthly") 2.dp else 1.dp,
                                color = if (selectedPlan == "monthly") NeonViolet else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable { selectedPlan = "monthly" },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedPlan == "monthly") NeonViolet.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isArabic) "شهري" else "Monthly",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$9.99/mo",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = if (isArabic) "تجديد شهري" else "Flexible billing",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Subscribe / Toggle Button
                Button(
                    onClick = {
                        onTogglePro(!isCurrentlyPro)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("subscribe_pro_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonViolet
                    )
                ) {
                    Text(
                        text = if (isCurrentlyPro) {
                            if (isArabic) "الاشتراك مفعل بالفعل 👑 (اضغط للإلغاء)" else "Pro Access Active 👑 (Tap to Switch)"
                        } else {
                            if (isArabic) "ترقية إلى PRO مع تجربة مجانية ✨" else "Upgrade to PRO (3-Day Free Trial) ✨"
                        },
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isArabic) "يمكنك الإلغاء في أي وقت من إعدادات المتجر."
                    else "Cancel anytime in Google Play Store settings.",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
