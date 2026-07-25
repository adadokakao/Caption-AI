package com.example.data.repository

import com.example.data.api.CaptionAiEngine
import com.example.data.local.CaptionDao
import com.example.data.local.CaptionEntity
import com.example.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray

class CaptionRepository(
    private val captionDao: CaptionDao,
    private val aiEngine: CaptionAiEngine = CaptionAiEngine(),
    val hashtagRepository: HashtagRepository = HashtagRepository()
) {

    val allSavedCaptions: Flow<List<CaptionEntity>> = captionDao.getAllCaptions()
    val favoriteCaptions: Flow<List<CaptionEntity>> = captionDao.getFavoriteCaptions()

    fun searchCaptions(query: String): Flow<List<CaptionEntity>> {
        return if (query.isBlank()) {
            captionDao.getAllCaptions()
        } else {
            captionDao.searchCaptions(query.trim())
        }
    }

    fun getCaptionsByPlatform(platform: SocialPlatform): Flow<List<CaptionEntity>> {
        return captionDao.getCaptionsByPlatform(platform.id)
    }

    suspend fun generateCaptions(options: CaptionOptions, customApiKey: String? = null): List<GeneratedCaption> {
        return aiEngine.generateCaptions(options, customApiKey)
    }

    suspend fun saveCaption(caption: GeneratedCaption, customTitle: String? = null): Long {
        val titleText = customTitle?.ifBlank { null }
            ?: caption.hook.take(30).ifBlank { "Saved Caption" }

        val tagsJson = JSONArray(caption.hashtags).toString()

        val entity = CaptionEntity(
            title = titleText,
            fullText = caption.getFullText(),
            hook = caption.hook,
            platform = caption.platform.id,
            style = caption.style.id,
            language = caption.language.code,
            hashtagsJson = tagsJson,
            isFavorite = true
        )
        return captionDao.insertCaption(entity)
    }

    suspend fun deleteCaption(id: Long) {
        captionDao.deleteCaptionById(id)
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        captionDao.updateFavorite(id, isFavorite)
    }

    suspend fun clearAllSavedCaptions() {
        captionDao.clearAll()
    }
}
