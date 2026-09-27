package com.shamtube.app.data.model

enum class Category(val arabic: String, val english: String) {
    ALL("الكل", "All"),
    NEWS("أخبار", "News"),
    ENTERTAINMENT("ترفيه", "Entertainment"),
    SPORTS("رياضة", "Sports"),
    GAMING("ألعاب", "Gaming"),
    EDUCATION("تعليم", "Education"),
    MUSIC("موسيقى", "Music"),
    MOVIES("أفلام", "Movies"),
    TECHNOLOGY("تقنية", "Technology"),
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
)

data class Channel(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val bannerUrl: String,
    val description: String,
    val subscribers: Long,
    val isSubscribed: Boolean = false,
)

data class Video(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val channelId: String,
    val channelName: String,
    val channelAvatar: String,
    val views: Long,
    val likes: Long,
    val comments: Int,
    val uploadDate: String,
    val duration: String,
    val category: Category,
    val tags: List<String>,
    val likedByMe: Boolean = false,
    val savedByMe: Boolean = false,
)

data class Comment(
    val id: String,
    val videoId: String,
    val userId: String,
    val userName: String,
    val userAvatar: String,
    val text: String,
    val likes: Long,
    val createdAt: String,
    val parentId: String? = null,
    val likedByMe: Boolean = false,
)

data class Playlist(
    val id: String,
    val name: String,
    val description: String,
    val videoCount: Int,
    val coverUrl: String,
)

data class Notification(
    val id: String,
    val title: String,
    val body: String,
    val type: NotificationType,
    val imageUrl: String,
    val timeLabel: String,
    val isRead: Boolean = false,
)

enum class NotificationType {
    NEW_VIDEO,
    NEW_SUBSCRIBER,
    LIKE,
    COMMENT,
    REPLY,
}

data class Subscription(
    val id: String,
    val channelId: String,
    val channelName: String,
    val channelAvatar: String,
    val subscribedAt: String,
)