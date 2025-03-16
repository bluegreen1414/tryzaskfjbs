package com.kapirti.social_chat_food_video.ui.presentation.camera.di

import com.kapirti.social_chat_food_video.ui.presentation.camera.CameraProviderManager
import com.kapirti.social_chat_food_video.ui.presentation.camera.CameraXProcessCameraProviderManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CameraProviderBindingModule {

    @Binds
    fun bindCameraProviderManager(manager: CameraXProcessCameraProviderManager): CameraProviderManager
}
