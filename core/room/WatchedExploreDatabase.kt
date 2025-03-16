package com.kapirti.social_chat_food_video.core.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WatchedExplore::class], version = 1)
abstract class WatchedExploreDatabase : RoomDatabase() {
    abstract fun watchedExploreDao(): WatchedExploreDao
}

@Database(entities = [WatchedExplorePhoto::class], version = 1)
abstract class WatchedExplorePhotoDatabase : RoomDatabase() {
    abstract fun watchedExplorePhotoDao(): WatchedExplorePhotoDao
}
