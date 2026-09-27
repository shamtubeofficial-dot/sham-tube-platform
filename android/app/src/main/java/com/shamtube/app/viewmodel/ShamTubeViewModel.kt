package com.shamtube.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shamtube.app.ShamTubeApplication
import com.shamtube.app.data.ShamTubeRepository
import com.shamtube.app.data.model.Category
import com.shamtube.app.data.model.Comment
import com.shamtube.app.data.model.Video
import com.shamtube.app.data.model.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ShamTubeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ShamTubeRepository =
        (application as ShamTubeApplication).repository

    val videos: StateFlow<List<Video>> = repository.videos.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList(),
    )
    val channels: StateFlow<List<Channel>> = repository.channels.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList(),
    )

    private val _selectedCategory = MutableStateFlow(Category.ALL)
    val selectedCategory: StateFlow<Category> = _selectedCategory.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _uploadMessage = MutableStateFlow<String?>(null)
    val uploadMessage: StateFlow<String?> = _uploadMessage.asStateFlow()

    private val commentFlows = mutableMapOf<String, StateFlow<List<Comment>>>()

    val feed: StateFlow<List<Video>> = combine(videos, selectedCategory) { allVideos, category ->
        if (category == Category.ALL) allVideos else allVideos.filter { it.category == category }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val searchResults: StateFlow<List<Video>> = combine(videos, query) { allVideos, term ->
        if (term.isBlank()) emptyList()
        else allVideos.filter {
            it.title.contains(term, ignoreCase = true) ||
                it.channelName.contains(term, ignoreCase = true) ||
                it.tags.any { tag -> tag.contains(term, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch { repository.seedIfNeeded() }
    }

    fun selectCategory(category: Category) {
        _selectedCategory.value = category
    }

    fun updateQuery(value: String) {
        _query.value = value
    }

    fun submitSearch(value: String = _query.value) {
        val cleanValue = value.trim()
        if (cleanValue.isBlank()) return
        _query.value = cleanValue
        _recentSearches.value = listOf(cleanValue) +
            _recentSearches.value.filterNot { it.equals(cleanValue, ignoreCase = true) }.take(4)
    }

    fun commentsFor(videoId: String): Flow<List<Comment>> =
        commentFlows.getOrPut(videoId) {
            repository.comments(videoId).stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList(),
            )
        }

    fun toggleLike(video: Video) {
        viewModelScope.launch { repository.setLike(video, !video.likedByMe) }
    }

    fun toggleSaved(video: Video) {
        viewModelScope.launch { repository.setSaved(video, !video.savedByMe) }
    }

    fun toggleCommentLike(comment: Comment) {
        viewModelScope.launch { repository.setCommentLike(comment, !comment.likedByMe) }
    }

    fun addComment(videoId: String, text: String, parentId: String? = null) {
        val cleanText = text.trim()
        if (cleanText.isBlank()) return
        viewModelScope.launch {
            repository.addComment(
                Comment(
                    id = UUID.randomUUID().toString(),
                    videoId = videoId,
                    userId = "local-user",
                    userName = "زائر شام تيوب",
                    userAvatar = "https://i.pravatar.cc/150?img=11",
                    text = cleanText,
                    likes = 0,
                    createdAt = "الآن",
                    parentId = parentId,
                ),
            )
        }
    }

    fun toggleSubscription(channel: Channel) {
        viewModelScope.launch { repository.setSubscription(channel, !channel.isSubscribed) }
    }

    fun uploadVideo(
        videoUri: String,
        thumbnailUri: String?,
        title: String,
        description: String,
        category: Category,
        tagsText: String,
    ) {
        if (title.isBlank()) {
            _uploadMessage.value = "أدخل عنوان الفيديو"
            return
        }
        viewModelScope.launch {
            _isUploading.value = true
            _uploadMessage.value = null
            repository.addVideo(
                Video(
                    id = "local-${UUID.randomUUID()}",
                    title = title.trim(),
                    description = description.trim(),
                    thumbnailUrl = thumbnailUri
                        ?: "https://images.unsplash.com/photo-1492619375914-88005aa9e8fb?w=1000",
                    videoUrl = videoUri,
                    channelId = "local-user-channel",
                    channelName = "قناتي",
                    channelAvatar = "https://i.pravatar.cc/150?img=11",
                    views = 0,
                    likes = 0,
                    comments = 0,
                    uploadDate = "الآن",
                    duration = "00:00",
                    category = category,
                    tags = tagsText.split(",").map(String::trim).filter(String::isNotBlank),
                ),
            )
            _isUploading.value = false
            _uploadMessage.value = "تم حفظ الفيديو محلياً"
        }
    }

    fun clearUploadMessage() {
        _uploadMessage.value = null
    }
}