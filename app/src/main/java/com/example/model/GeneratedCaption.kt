package com.example.model

data class GeneratedCaption(
    val id: String = java.util.UUID.randomUUID().toString(),
    val hook: String,
    val body: String,
    val callToAction: String? = null,
    val hashtags: List<String> = emptyList(),
    val platform: SocialPlatform = SocialPlatform.INSTAGRAM,
    val style: CaptionStyle = CaptionStyle.VIRAL,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val estimatedViralScore: Int = 92, // 1-100
    val hookScore: Int = 95,
    val engagementScore: Int = 90,
    val seoScore: Int = 94,
    val qualityTips: List<String> = listOf("High scroll-stopping hook", "Clear call to action", "Optimal hashtag density"),
    val isSaved: Boolean = false
) {
    fun getFullText(): String {
        val builder = StringBuilder()
        if (hook.isNotBlank()) {
            builder.append(hook.trim()).append("\n\n")
        }
        if (body.isNotBlank()) {
            builder.append(body.trim())
        }
        if (!callToAction.isNullOrBlank()) {
            builder.append("\n\n").append(callToAction.trim())
        }
        if (hashtags.isNotEmpty()) {
            builder.append("\n\n").append(hashtags.joinToString(" ") { if (it.startsWith("#")) it else "#$it" })
        }
        return builder.toString().trim()
    }

    fun getInstagramFormattedText(): String {
        val builder = StringBuilder()
        if (hook.isNotBlank()) {
            builder.append(hook.trim()).append("\n.\n.\n")
        }
        if (body.isNotBlank()) {
            builder.append(body.trim()).append("\n.\n.\n")
        }
        if (!callToAction.isNullOrBlank()) {
            builder.append(callToAction.trim()).append("\n.\n.\n")
        }
        if (hashtags.isNotEmpty()) {
            builder.append(hashtags.joinToString(" ") { if (it.startsWith("#")) it else "#$it" })
        }
        return builder.toString().trim()
    }

    fun getTikTokFormattedText(): String {
        val tags = hashtags.take(5).joinToString(" ") { if (it.startsWith("#")) it else "#$it" }
        return "${hook.trim()} ${body.trim()} $tags".trim()
    }

    fun getLinkedInFormattedText(): String {
        val builder = StringBuilder()
        if (hook.isNotBlank()) {
            builder.append("🚀 ").append(hook.trim()).append("\n\n")
        }
        if (body.isNotBlank()) {
            val lines = body.trim().split("\n")
            lines.forEach { line ->
                if (line.isNotBlank()) {
                    builder.append("• ").append(line.trim()).append("\n")
                }
            }
            builder.append("\n")
        }
        if (!callToAction.isNullOrBlank()) {
            builder.append("💡 ").append(callToAction.trim()).append("\n\n")
        }
        if (hashtags.isNotEmpty()) {
            builder.append(hashtags.joinToString(" ") { if (it.startsWith("#")) it else "#$it" })
        }
        return builder.toString().trim()
    }

    fun toJsonString(): String {
        val tagsJson = hashtags.joinToString("\",\"", "[\"", "\"]")
        return """
            {
              "hook": "${hook.replace("\"", "\\\"")}",
              "body": "${body.replace("\"", "\\\"")}",
              "callToAction": "${(callToAction ?: "").replace("\"", "\\\"")}",
              "hashtags": $tagsJson,
              "viralScore": $estimatedViralScore,
              "platform": "${platform.name}",
              "language": "${language.code}"
            }
        """.trimIndent()
    }
}

