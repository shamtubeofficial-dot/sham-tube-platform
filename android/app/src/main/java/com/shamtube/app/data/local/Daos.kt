package com.shamtube.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos ORDER BY id DESC")
    fun observeVideos(): Flow<List<VideoEntity>>

    @Query("SELECT COUNT(*) FROM videos")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videos: List<VideoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: VideoEntity)

    @Query("UPDATE videos SET likedByMe = :liked, likes = :likes WHERE id = :videoId")
    suspend fun updateLike(videoId: String, liked: Boolean, likes: Long)

    @Query("UPDATE videos SET savedByMe = :saved WHERE id = :videoId")
    suspend fun updateSaved(videoId: String, saved: Boolean)

    @Query("UPDATE videos SET comments = comments + 1 WHERE id = :videoId")
    suspend fun incrementCommentCount(videoId: String)
}

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE videoId = :videoId ORDER BY createdAt DESC")
    fun observeForVideo(videoId: String): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: CommentEntity)

    @Query("UPDATE comments SET likedByMe = :liked, likes = :likes WHERE id = :commentId")
    suspend fun updateLike(commentId: String, liked: Boolean, likes: Long)
}

@Dao
interface ChannelDao {
    @Query("SELECT * FROM channels ORDER BY name")
    fun observeChannels(): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM channels WHERE id = :channelId LIMIT 1")
    fun observeChannel(channelId: String): Flow<ChannelEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(channels: List<ChannelEntity>)

    @Query("UPDATE channels SET isSubscribed = :subscribed, subscribers = :subscribers WHERE id = :channelId")
    suspend fun updateSubscription(channelId: String, subscribed: Boolean, subscribers: Long)
}