package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CaptionDao {
    @Query("SELECT * FROM saved_captions ORDER BY timestamp DESC")
    fun getAllCaptions(): Flow<List<CaptionEntity>>

    @Query("SELECT * FROM saved_captions WHERE fullText LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchCaptions(query: String): Flow<List<CaptionEntity>>

    @Query("SELECT * FROM saved_captions WHERE platform = :platform ORDER BY timestamp DESC")
    fun getCaptionsByPlatform(platform: String): Flow<List<CaptionEntity>>

    @Query("SELECT * FROM saved_captions WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteCaptions(): Flow<List<CaptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaption(caption: CaptionEntity): Long

    @Query("DELETE FROM saved_captions WHERE id = :id")
    suspend fun deleteCaptionById(id: Long)

    @Query("UPDATE saved_captions SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    @Query("DELETE FROM saved_captions")
    suspend fun clearAll()
}
