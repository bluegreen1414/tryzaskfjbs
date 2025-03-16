package com.kapirti.social_chat_food_video.ui.presentation.camera

import android.annotation.SuppressLint
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.SurfaceRequest.TransformationInfo as CXTransformationInfo
import androidx.camera.viewfinder.compose.Viewfinder
import androidx.camera.viewfinder.surface.ImplementationMode
import androidx.camera.viewfinder.surface.TransformationInfo
import androidx.camera.viewfinder.surface.ViewfinderSurfaceRequest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
@Composable
fun CameraXViewfinder(
    modifier: Modifier = Modifier,
    implementationMode: ImplementationMode = ImplementationMode.PERFORMANCE,
    onSurfaceProviderReady: (Preview.SurfaceProvider) -> Unit = {},
) {
    val viewfinderArgs by produceState<ViewfinderArgs?>(initialValue = null, implementationMode) {
        val requests = MutableStateFlow<SurfaceRequest?>(null)
        onSurfaceProviderReady(
            Preview.SurfaceProvider { request ->
                requests.update { oldRequest ->
                    oldRequest?.willNotProvideSurface()
                    request
                }
            },
        )

        requests.filterNotNull().collectLatest { request ->
            val viewfinderSurfaceRequest = ViewfinderSurfaceRequest.Builder(request.resolution)
                .build()

            request.addRequestCancellationListener(Runnable::run) {
                viewfinderSurfaceRequest.markSurfaceSafeToRelease()
            }

            // Launch undispatched so we always reach the try/finally in this coroutine
            launch(start = CoroutineStart.UNDISPATCHED) {
                try {
                    val surface = viewfinderSurfaceRequest.getSurface()
                    request.provideSurface(surface, Runnable::run) {
                        viewfinderSurfaceRequest.markSurfaceSafeToRelease()
                    }
                } finally {
                    // If we haven't provided the surface, such as if we're cancelled
                    // while suspending on getSurface(), this call will succeed. Otherwise
                    // it will be a no-op.
                    request.willNotProvideSurface()
                }
            }

            val transformationInfos = MutableStateFlow<CXTransformationInfo?>(null)
            request.setTransformationInfoListener(Runnable::run) {
                transformationInfos.value = it
            }

            transformationInfos.filterNotNull().collectLatest {
                value = ViewfinderArgs(
                    viewfinderSurfaceRequest,
                    implementationMode,
                    TransformationInfo(
                        it.rotationDegrees,
                        it.cropRect.left,
                        it.cropRect.right,
                        it.cropRect.top,
                        it.cropRect.bottom,
                        it.isMirroring,
                    ),
                )
            }
        }
    }

    viewfinderArgs?.let { args ->
        Viewfinder(
            surfaceRequest = args.viewfinderSurfaceRequest,
            implementationMode = args.implementationMode,
            transformationInfo = args.transformationInfo,
            modifier = modifier.fillMaxSize(),
        )
    }
}

private data class ViewfinderArgs(
    val viewfinderSurfaceRequest: ViewfinderSurfaceRequest,
    val implementationMode: ImplementationMode,
    val transformationInfo: TransformationInfo,
)
