package com.example.model

import androidx.compose.ui.graphics.Color

enum class SocialPlatform(
    val id: String,
    val displayName: String,
    val iconName: String,
    val brandColor: Color,
    val hashtagRecommendation: String,
    val maxLength: Int
) {
    INSTAGRAM(
        id = "instagram",
        displayName = "Instagram",
        iconName = "instagram",
        brandColor = Color(0xFFE1306C),
        hashtagRecommendation = "5-10 targeted hashtags",
        maxLength = 2200
    ),
    TIKTOK(
        id = "tiktok",
        displayName = "TikTok",
        iconName = "tiktok",
        brandColor = Color(0xFF00F2FE),
        hashtagRecommendation = "3-5 high volume hashtags",
        maxLength = 4000
    ),
    YOUTUBE_SHORTS(
        id = "youtube_shorts",
        displayName = "YouTube Shorts",
        iconName = "youtube",
        brandColor = Color(0xFFFF0000),
        hashtagRecommendation = "2-3 core hashtags in title/description",
        maxLength = 1000
    ),
    FACEBOOK(
        id = "facebook",
        displayName = "Facebook",
        iconName = "facebook",
        brandColor = Color(0xFF1877F2),
        hashtagRecommendation = "1-3 subtle hashtags",
        maxLength = 5000
    ),
    TWITTER_X(
        id = "twitter_x",
        displayName = "X (Twitter)",
        iconName = "twitter",
        brandColor = Color(0xFF000000),
        hashtagRecommendation = "1-2 relevant hashtags",
        maxLength = 280
    ),
    LINKEDIN(
        id = "linkedin",
        displayName = "LinkedIn",
        iconName = "linkedin",
        brandColor = Color(0xFF0A66C2),
        hashtagRecommendation = "3-5 industry hashtags",
        maxLength = 3000
    );

    companion object {
        fun fromId(id: String): SocialPlatform {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: INSTAGRAM
        }
    }
}
