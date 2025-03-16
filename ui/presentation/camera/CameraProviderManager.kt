package com.kapirti.social_chat_food_video.ui.presentation.camera

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.concurrent.futures.await
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface CameraProviderManager {
    suspend fun getCameraProvider(): ProcessCameraProvider
}

class CameraXProcessCameraProviderManager @Inject constructor(@ApplicationContext context: Context) :
    CameraProviderManager {
    private val cameraFuture = ProcessCameraProvider.getInstance(context)
    override suspend fun getCameraProvider() = cameraFuture.await()
}
