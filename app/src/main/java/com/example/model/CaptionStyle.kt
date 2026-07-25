package com.example.model

enum class CaptionStyle(
    val id: String,
    val displayNameEn: String,
    val displayNameAr: String,
    val description: String,
    val iconSymbol: String
) {
    VIRAL(
        id = "viral",
        displayNameEn = "Viral",
        displayNameAr = "انتشاري / فيرال",
        description = "Attention-grabbing hooks designed for algorithm boost",
        iconSymbol = "⚡"
    ),
    FUNNY(
        id = "funny",
        displayNameEn = "Funny",
        displayNameAr = "مضحك / فكاهي",
        description = "Humorous, witty, and relatable punchlines",
        iconSymbol = "😂"
    ),
    EMOTIONAL(
        id = "emotional",
        displayNameEn = "Emotional",
        displayNameAr = "عاطفي / مؤشر",
        description = "Heartfelt, deep connection and relatable feelings",
        iconSymbol = "❤️"
    ),
    PROFESSIONAL(
        id = "professional",
        displayNameEn = "Professional",
        displayNameAr = "احترافي",
        description = "Polished, authoritative, and structured tone",
        iconSymbol = "💼"
    ),
    INSPIRATIONAL(
        id = "inspirational",
        displayNameEn = "Inspirational",
        displayNameAr = "إلهامي / تحفيزي",
        description = "Motivating, uplifting, and empowering thoughts",
        iconSymbol = "✨"
    ),
    MARKETING(
        id = "marketing",
        displayNameEn = "Marketing",
        displayNameAr = "تسويقي / ترويجي",
        description = "Call-to-action focused for sales and conversions",
        iconSymbol = "🚀"
    ),
    STORYTELLING(
        id = "storytelling",
        displayNameEn = "Storytelling",
        displayNameAr = "قصصي",
        description = "Engaging narrative hook, context, and takeaway",
        iconSymbol = "📖"
    );

    fun getDisplayName(language: AppLanguage): String {
        return if (language == AppLanguage.ARABIC) displayNameAr else displayNameEn
    }

    companion object {
        fun fromId(id: String): CaptionStyle {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: VIRAL
        }
    }
}
