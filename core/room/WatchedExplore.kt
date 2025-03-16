package com.kapirti.social_chat_food_video.core.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watched_explore")
data class WatchedExplore(
    @PrimaryKey val exploreId: String
)

@Entity(tableName = "watched_explore_photo")
data class WatchedExplorePhoto(
    @PrimaryKey val exploreId: String
)
