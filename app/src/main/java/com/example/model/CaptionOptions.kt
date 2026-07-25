package com.example.model

data class CaptionOptions(
    val topicOrIdea: String,
    val platform: SocialPlatform = SocialPlatform.INSTAGRAM,
    val style: CaptionStyle = CaptionStyle.VIRAL,
    val tone: CaptionTone = CaptionTone.CASUAL,
    val length: CaptionLength = CaptionLength.MEDIUM,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val includeEmojis: Boolean = true,
    val includeHashtags: Boolean = true,
    val includeCta: Boolean = true,
    val imageBase64: String? = null // For vision mode
)
