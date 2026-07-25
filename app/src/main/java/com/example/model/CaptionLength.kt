package com.example.model

enum class CaptionLength(
    val id: String,
    val displayNameEn: String,
    val displayNameAr: String,
    val description: String
) {
    SHORT("short", "Short (< 15 words)", "قصير (< 15 كلمة)", "Quick punchy line"),
    MEDIUM("medium", "Medium (15 - 50 words)", "متوسط (15 - 50 كلمة)", "Balanced context & hashtags"),
    LONG("long", "Long (> 50 words)", "طويل (> 50 كلمة)", "Detailed narrative & value drop");

    fun getDisplayName(language: AppLanguage): String {
        return if (language == AppLanguage.ARABIC) displayNameAr else displayNameEn
    }

    companion object {
        fun fromId(id: String): CaptionLength {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: MEDIUM
        }
    }
}
