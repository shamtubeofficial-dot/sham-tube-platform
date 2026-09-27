package com.shamtube.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [VideoEntity::class, CommentEntity::class, ChannelEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class ShamTubeDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    abstract fun commentDao(): CommentDao
    abstract fun channelDao(): ChannelDao

    companion object {
        fun create(context: Context): ShamTubeDatabase = Room.databaseBuilder(
            context,
            ShamTubeDatabase::class.java,
            "sham_tube.db",
        ).build()
    }
}