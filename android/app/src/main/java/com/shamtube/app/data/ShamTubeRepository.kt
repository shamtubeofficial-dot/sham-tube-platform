package com.shamtube.app.data

import com.shamtube.app.data.local.ChannelEntity
import com.shamtube.app.data.local.CommentDao
import com.shamtube.app.data.local.CommentEntity
import com.shamtube.app.data.local.ShamTubeDatabase
import com.shamtube.app.data.local.VideoDao
import com.shamtube.app.data.local.toEntity
import com.shamtube.app.data.local.toModel
import com.shamtube.app.data.model.Channel
import com.shamtube.app.data.model.Comment
import com.shamtube.app.data.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ShamTubeRepository {
    val videos: Flow<List<Video>>
    val channels: Flow<List<Channel>>
    fun comments(videoId: String): Flow<List<Comment>>
    suspend fun seedIfNeeded()
    suspend fun addVideo(video: Video)
    suspend fun setLike(video: Video, liked: Boolean)
    suspend fun setSaved(video: Video, saved: Boolean)
    suspend fun addComment(comment: Comment)
    suspend fun setCommentLike(comment: Comment, liked: Boolean)
    suspend fun setSubscription(channel: Channel, subscribed: Boolean)
}

class LocalShamTubeRepository(
    private val database: ShamTubeDatabase,
) : ShamTubeRepository {
    private val videoDao: VideoDao = database.videoDao()
    private val commentDao: CommentDao = database.commentDao()

    override val videos: Flow<List<Video>> = videoDao.observeVideos().map { list ->
        list.map { it.toModel() }
    }

    override val channels: Flow<List<Channel>> = database.channelDao().observeChannels().map { list ->
        list.map {
            Channel(
                id = it.id,
                name = it.name,
                avatarUrl = it.avatarUrl,
                bannerUrl = it.bannerUrl,
                description = it.description,
                subscribers = it.subscribers,
                isSubscribed = it.isSubscribed,
            )
        }
    }

    override fun comments(videoId: String): Flow<List<Comment>> =
        commentDao.observeForVideo(videoId).map { list -> list.map { it.toModel() } }

    override suspend fun seedIfNeeded() {
        if (videoDao.count() == 0) {
            database.channelDao().insertAll(SampleData.channels)
            videoDao.insertAll(SampleData.videos)
            SampleData.comments.forEach(commentDao::insert)
        }
    }

    override suspend fun addVideo(video: Video) {
        videoDao.insert(video.toEntity())
    }

    override suspend fun setLike(video: Video, liked: Boolean) {
        val newLikes = (video.likes + if (liked) 1 else -1).coerceAtLeast(0)
        videoDao.updateLike(video.id, liked, newLikes)
    }

    override suspend fun setSaved(video: Video, saved: Boolean) {
        videoDao.updateSaved(video.id, saved)
    }

    override suspend fun addComment(comment: Comment) {
        commentDao.insert(comment.toEntity())
        videoDao.incrementCommentCount(comment.videoId)
    }

    override suspend fun setCommentLike(comment: Comment, liked: Boolean) {
        val newLikes = (comment.likes + if (liked) 1 else -1).coerceAtLeast(0)
        commentDao.updateLike(comment.id, liked, newLikes)
    }

    override suspend fun setSubscription(channel: Channel, subscribed: Boolean) {
        val newSubscribers = (channel.subscribers + if (subscribed) 1 else -1).coerceAtLeast(0)
        database.channelDao().updateSubscription(channel.id, subscribed, newSubscribers)
    }
}