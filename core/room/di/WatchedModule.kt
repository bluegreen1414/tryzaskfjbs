package com.kapirti.social_chat_food_video.core.room.di

import android.content.Context
import androidx.room.Room
import com.kapirti.social_chat_food_video.core.room.WatchedExploreDao
import com.kapirti.social_chat_food_video.core.room.WatchedExploreDatabase
import com.kapirti.social_chat_food_video.core.room.WatchedExplorePhotoDao
import com.kapirti.social_chat_food_video.core.room.WatchedExplorePhotoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WatchedModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): WatchedExploreDatabase {
        return Room.databaseBuilder(
            context,
            WatchedExploreDatabase::class.java,
            "watched_explore_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideWatchedExplore(database: WatchedExploreDatabase): WatchedExploreDao {
        return database.watchedExploreDao()
    }
}


@Module
@InstallIn(SingletonComponent::class)
object WatchedPhotoModule {

    @Provides
    @Singleton
    fun provideAppDatabasePhoto(@ApplicationContext context: Context): WatchedExplorePhotoDatabase {
        return Room.databaseBuilder(
            context,
            WatchedExplorePhotoDatabase::class.java,
            "watched_explore_photo_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideWatchedExplorePhoto(database: WatchedExplorePhotoDatabase): WatchedExplorePhotoDao {
        return database.watchedExplorePhotoDao()
    }
}
