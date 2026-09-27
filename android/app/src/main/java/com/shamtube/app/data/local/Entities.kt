package com.shamtube.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shamtube.app.data.model.Category
import com.shamtube.app.data.model.Comment
import com.shamtube.app.data.model.Video

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey val id: String,
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
    val category: String,
    val tags: String,
    val likedByMe: Boolean = false,
    val savedByMe: Boolean = false,
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey val id: String,
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

@Entity(tableName = "channels")
data class ChannelEntity(
    @PrimaryKey val id: String,
    val name: String,
    val avatarUrl: String,
    val bannerUrl: String,
    val description: String,
    val subscribers: Long,
    val isSubscribed: Boolean = false,
)

fun VideoEntity.toModel(): Video = Video(
    id = id,
    title = title,
    description = description,
    thumbnailUrl = thumbnailUrl,
    videoUrl = videoUrl,
    channelId = channelId,
    channelName = channelName,
    channelAvatar = channelAvatar,
    views = views,
    likes = likes,
    comments = comments,
    uploadDate = uploadDate,
    duration = duration,
    category = Category.entries.firstOrNull { it.name == category } ?: Category.ENTERTAINMENT,
    tags = tags.split("|").filter(String::isNotBlank),
    likedByMe = likedByMe,
    savedByMe = savedByMe,
)

fun Video.toEntity(): VideoEntity = VideoEntity(
    id = id,
    title = title,
    description = description,
    thumbnailUrl = thumbnailUrl,
    videoUrl = videoUrl,
    channelId = channelId,
    channelName = channelName,
    channelAvatar = channelAvatar,
    views = views,
    likes = likes,
    comments = comments,
    uploadDate = uploadDate,
    duration = duration,
    category = category.name,
    tags = tags.joinToString("|"),
    likedByMe = likedByMe,
    savedByMe = savedByMe,
)

fun CommentEntity.toModel(): Comment = Comment(
    id = id,
    videoId = videoId,
    userId = userId,
    userName = userName,
    userAvatar = userAvatar,
    text = text,
    likes = likes,
    createdAt = createdAt,
    parentId = parentId,
    likedByMe = likedByMe,
)

fun Comment.toEntity(): CommentEntity = CommentEntity(
    id = id,
    videoId = videoId,
    userId = userId,
    userName = userName,
    userAvatar = userAvatar,
    text = text,
    likes = likes,
    createdAt = createdAt,
    parentId = parentId,
    likedByMe = likedByMe,
)