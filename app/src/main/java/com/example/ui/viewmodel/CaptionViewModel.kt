package com.example.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CaptionDatabase
import com.example.data.local.CaptionEntity
import com.example.data.repository.CaptionRepository
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

sealed interface GenerationUiState {
    object Idle : GenerationUiState
    object Loading : GenerationUiState
    data class Success(val captions: List<GeneratedCaption>) : GenerationUiState
    data class Error(val message: String) : GenerationUiState
}

class CaptionViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CaptionDatabase.getDatabase(application)
    private val repository = CaptionRepository(db.captionDao())

    // Options state
    private val _options = MutableStateFlow(CaptionOptions(topicOrIdea = ""))
    val options: StateFlow<CaptionOptions> = _options.asStateFlow()

    // Generation UI State
    private val _generationState = MutableStateFlow<GenerationUiState>(GenerationUiState.Idle)
    val generationState: StateFlow<GenerationUiState> = _generationState.asStateFlow()

    // Vision Image State
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    private val _selectedBitmap = MutableStateFlow<Bitmap?>(null)
    val selectedBitmap: StateFlow<Bitmap?> = _selectedBitmap.asStateFlow()

    // Saved Captions Search & Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSavedPlatform = MutableStateFlow<SocialPlatform?>(null)
    val selectedSavedPlatform: StateFlow<SocialPlatform?> = _selectedSavedPlatform.asStateFlow()

    val savedCaptions: StateFlow<List<CaptionEntity>> = _searchQuery
        .combine(_selectedSavedPlatform) { query, platform -> Pair(query, platform) }
        .flatMapLatest { (query, platform) ->
            repository.searchCaptions(query).map { list ->
                if (platform != null) {
                    list.filter { it.platform == platform.id }
                } else {
                    list
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // User preferences & Subscription
    private val _customApiKey = MutableStateFlow("")
    val customApiKey: StateFlow<String> = _customApiKey.asStateFlow()

    private val _isProUser = MutableStateFlow(true) // Set to Pro mode enabled for full showcase experience
    val isProUser: StateFlow<Boolean> = _isProUser.asStateFlow()

    private val _showProPaywall = MutableStateFlow(false)
    val showProPaywall: StateFlow<Boolean> = _showProPaywall.asStateFlow()

    fun toggleProStatus(enabled: Boolean) {
        _isProUser.value = enabled
    }

    fun showProPaywallDialog() {
        _showProPaywall.value = true
    }

    fun dismissProPaywallDialog() {
        _showProPaywall.value = false
    }


    fun updateTopic(topic: String) {
        _options.update { it.copy(topicOrIdea = topic) }
    }

    fun updatePlatform(platform: SocialPlatform) {
        _options.update { it.copy(platform = platform) }
    }

    fun updateStyle(style: CaptionStyle) {
        _options.update { it.copy(style = style) }
    }

    fun updateTone(tone: CaptionTone) {
        _options.update { it.copy(tone = tone) }
    }

    fun updateLength(length: CaptionLength) {
        _options.update { it.copy(length = length) }
    }

    fun updateLanguage(language: AppLanguage) {
        _options.update { it.copy(language = language) }
    }

    fun toggleEmojis(enabled: Boolean) {
        _options.update { it.copy(includeEmojis = enabled) }
    }

    fun toggleHashtags(enabled: Boolean) {
        _options.update { it.copy(includeHashtags = enabled) }
    }

    fun toggleCta(enabled: Boolean) {
        _options.update { it.copy(includeCta = enabled) }
    }

    fun setCustomApiKey(key: String) {
        _customApiKey.value = key
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSavedPlatformFilter(platform: SocialPlatform?) {
        _selectedSavedPlatform.value = platform
    }

    fun generateCaptions() {
        val currentTopic = _options.value.topicOrIdea
        if (currentTopic.isBlank() && _selectedBitmap.value == null) {
            _generationState.value = GenerationUiState.Error("Please enter a topic or upload an image.")
            return
        }

        viewModelScope.launch {
            _generationState.value = GenerationUiState.Loading
            try {
                var opts = _options.value
                if (_selectedBitmap.value != null) {
                    val base64 = bitmapToBase64(_selectedBitmap.value!!)
                    opts = opts.copy(imageBase64 = base64)
                }

                val results = repository.generateCaptions(opts, _customApiKey.value)
                _generationState.value = GenerationUiState.Success(results)
            } catch (e: Exception) {
                _generationState.value = GenerationUiState.Error("Failed to generate captions: ${e.localizedMessage}")
            }
        }
    }

    fun setImageFromUri(context: Context, uri: Uri) {
        _selectedImageUri.value = uri
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            _selectedBitmap.value = bitmap
            if (_options.value.topicOrIdea.isBlank()) {
                val isAr = _options.value.language == AppLanguage.ARABIC
                updateTopic(if (isAr) "صورة تم رفعها للتعديل" else "Uploaded photo analysis")
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error loading image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun setImageBitmap(bitmap: Bitmap) {
        _selectedBitmap.value = bitmap
        _selectedImageUri.value = null
        if (_options.value.topicOrIdea.isBlank()) {
            val isAr = _options.value.language == AppLanguage.ARABIC
            updateTopic(if (isAr) "صورة العينة للتحليل" else "Sample photo analysis")
        }
    }

    fun clearImage() {
        _selectedImageUri.value = null
        _selectedBitmap.value = null
        _options.update { it.copy(imageBase64 = null) }
    }

    fun saveCaption(caption: GeneratedCaption, context: Context) {
        viewModelScope.launch {
            repository.saveCaption(caption)
            val msg = if (caption.language == AppLanguage.ARABIC) "تم حفظ الكابشن بنجاح! ✨" else "Caption saved to Favorites! ✨"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteSavedCaption(id: Long, context: Context) {
        viewModelScope.launch {
            repository.deleteCaption(id)
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Caption AI", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to Clipboard! 📋", Toast.LENGTH_SHORT).show()
    }

    fun shareCaption(context: Context, text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Caption via")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }
}
