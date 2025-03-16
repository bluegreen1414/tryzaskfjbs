package com.kapirti.social_chat_food_video.core.data.di

import com.kapirti.social_chat_food_video.core.data.ConnectivityManagerNetworkMonitor
import com.kapirti.social_chat_food_video.core.data.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor
}
