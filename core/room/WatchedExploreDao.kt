package com.kapirti.social_chat_food_video.core.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WatchedExploreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: WatchedExplore)

    @Query("SELECT exploreId FROM watched_explore")
    suspend fun getWatchedExploreIds(): List<String>
}

@Dao
interface WatchedExplorePhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: WatchedExplorePhoto)

    @Query("SELECT exploreId FROM watched_explore_photo")
    suspend fun getWatchedExploreIds(): List<String>
}
