package com.example.model

enum class CaptionTone(
    val id: String,
    val displayNameEn: String,
    val displayNameAr: String,
    val emoji: String
) {
    CASUAL("casual", "Casual & Friendly", "ودي وبارد", "😊"),
    FORMAL("formal", "Formal & Direct", "رسمي ومباشر", "👔"),
    ENERGETIC("energetic", "Energetic & Hyped", "حماسي ومندفع", "🔥"),
    SARCASTIC("sarcastic", "Sarcastic & Witty", "ساخر وذكي", "😏"),
    EMPATHETIC("empathetic", "Empathetic & Warm", "تعاطفي ودافئ", "🤗"),
    PERSUASIVE("persuasive", "Persuasive & Bold", "مقنع وجريء", "🎯"),
    MINIMALIST("minimalist", "Minimalist & Clean", "بسيط ونظيف", "🌿");

    fun getDisplayName(language: AppLanguage): String {
        return if (language == AppLanguage.ARABIC) displayNameAr else displayNameEn
    }

    companion object {
        fun fromId(id: String): CaptionTone {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: CASUAL
        }
    }
}
