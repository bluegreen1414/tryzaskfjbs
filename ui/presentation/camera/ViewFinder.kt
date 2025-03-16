package com.kapirti.social_chat_food_video.ui.presentation.camera

import androidx.camera.core.Preview
import androidx.camera.viewfinder.surface.ImplementationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ViewFinder(
    cameraState: CameraState,
    onSurfaceProviderReady: (Preview.SurfaceProvider) -> Unit = {},
    onZoomChange: (Float) -> Unit,
) {
    val transformableState = rememberTransformableState(
        onTransformation = { zoomChange, _, _ ->
            onZoomChange(zoomChange)
        },
    )
    Box(
        Modifier
            .background(Color.Black)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .transformable(state = transformableState),
        ) {
            CameraXViewfinder(
                modifier = Modifier.fillMaxSize(),
                implementationMode = ImplementationMode.PERFORMANCE,
                onSurfaceProviderReady = onSurfaceProviderReady,
            )
        }
    }
}
