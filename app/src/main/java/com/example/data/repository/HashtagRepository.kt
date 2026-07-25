package com.example.data.repository

import com.example.model.AppLanguage

data class HashtagCategory(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val icon: String,
    val tags: List<String>
)

class HashtagRepository {

    val categories = listOf(
        HashtagCategory(
            id = "trending",
            nameEn = "🔥 Trending Now",
            nameAr = "🔥 الرائج الآن",
            icon = "🔥",
            tags = listOf(
                "#viral", "#explorepage", "#trending", "#fyp", "#shorts",
                "#reels", "#contentcreator", "#aesthetic", "#foryou", "#dailyvibes",
                "#mindset", "#growth", "#creators", "#subscribers", "#goviral"
            )
        ),
        HashtagCategory(
            id = "tech",
            nameEn = "💻 Tech & AI",
            nameAr = "💻 التكنولوجيا والذكاء الاصطناعي",
            icon = "💻",
            tags = listOf(
                "#ai", "#tech", "#innovation", "#software", "#coding",
                "#machinelearning", "#chatgpt", "#gemini", "#futuretech", "#cybersecurity",
                "#technews", "#developer", "#web3", "#gadgets", "#apps"
            )
        ),
        HashtagCategory(
            id = "fitness",
            nameEn = "💪 Fitness & Health",
            nameAr = "💪 اللياقة والصحة",
            icon = "💪",
            tags = listOf(
                "#fitnessmotivation", "#gymlife", "#workout", "#healthy", "#fitfam",
                "#bodybuilding", "#cardio", "#crossfit", "#nutrition", "#wellness",
                "#training", "#noexcuses", "#healthylifestyle", "#gains", "#fitnessgoals"
            )
        ),
        HashtagCategory(
            id = "travel",
            nameEn = "✈️ Travel & Lifestyle",
            nameAr = "✈️ السفر ونمط الحياة",
            icon = "✈️",
            tags = listOf(
                "#travelgram", "#wanderlust", "#travelphotography", "#nature", "#instatravel",
                "#adventure", "#explore", "#vacation", "#travelblogger", "#beautifuldestinations",
                "#landscape", "#passionpassport", "#citybreak", "#bucketlist", "#roamtheplanet"
            )
        ),
        HashtagCategory(
            id = "food",
            nameEn = "🍕 Food & Culinary",
            nameAr = "🍕 الطعام والطبخ",
            icon = "🍕",
            tags = listOf(
                "#foodie", "#instafood", "#yummy", "#delicious", "#foodphotography",
                "#foodstagram", "#homemade", "#chef", "#recipe", "#foodblogger",
                "#dinner", "#dessert", "#comfortfood", "#eatingfortheinsta", "#tasty"
            )
        ),
        HashtagCategory(
            id = "fashion",
            nameEn = "✨ Fashion & Style",
            nameAr = "✨ الموضة والجمال",
            icon = "✨",
            tags = listOf(
                "#fashion", "#ootd", "#style", "#instafashion", "#streetwear",
                "#outfitoftheday", "#lookbook", "#stylish", "#fashionblogger", "#beauty",
                "#mensfashion", "#womensfashion", "#aesthetic", "#trends", "#model"
            )
        ),
        HashtagCategory(
            id = "business",
            nameEn = "🚀 Business & Entrepreneurship",
            nameAr = "🚀 الأعمال والريادة",
            icon = "🚀",
            tags = listOf(
                "#entrepreneur", "#business", "#marketing", "#success", "#motivation",
                "#smallbusiness", "#startup", "#hustle", "#leadership", "#digitalmarketing",
                "#mindset", "#branding", "#money", "#finance", "#sales"
            )
        ),
        HashtagCategory(
            id = "art",
            nameEn = "🎨 Art & Photography",
            nameAr = "🎨 الفن والتصوير",
            icon = "🎨",
            tags = listOf(
                "#art", "#artist", "#artwork", "#photography", "#illustration",
                "#photooftheday", "#design", "#creative", "#digitalart", "#drawing",
                "#artistsoninstagram", "#visualart", "#graphicdesign", "#sketch", "#picoftheday"
            )
        )
    )

    fun generateHashtagsForTopic(topic: String, language: AppLanguage): List<String> {
        val normalized = topic.lowercase()
        val tags = mutableSetOf<String>()

        // Extract keywords from topic
        val words = normalized.split(Regex("\\s+")).map { it.replace(Regex("[^a-zA-Z0-9\u0600-\u06FF]"), "") }.filter { it.length > 2 }
        for (word in words) {
            if (language == AppLanguage.ARABIC) {
                tags.add("#$word")
            } else {
                tags.add("#$word")
            }
        }

        // Match category
        val matchedCategory = categories.find { cat ->
            cat.id in normalized || cat.tags.any { tag -> tag.replace("#", "").contains(normalized) }
        } ?: categories.first()

        tags.addAll(matchedCategory.tags.shuffled().take(10))

        if (language == AppLanguage.ARABIC) {
            tags.addAll(listOf("#محتوى", "#صناع_المحتوى", "#تطوير_الذات", "#إكسبلور", "#فيديو"))
        }

        return tags.toList().take(20)
    }
}
