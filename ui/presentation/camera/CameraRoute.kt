package com.kapirti.social_chat_food_video.ui.presentation.camera

import android.Manifest
import android.annotation.SuppressLint
import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.view.RotationProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraRoute(
    navigatePhotoPicker: () -> Unit,
    onMediaCaptured: (Media?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CameraViewModel = hiltViewModel(),
) {
    var surfaceProvider by remember { mutableStateOf<Preview.SurfaceProvider?>(null) }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    var captureMode by remember { mutableStateOf(CaptureMode.PHOTO) }
    val cameraAndRecordAudioPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ),
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    var isLayoutUnfolded by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(lifecycleOwner, context) {
        val windowInfoTracker = WindowInfoTracker.getOrCreate(context)
        windowInfoTracker.windowLayoutInfo(context).collect { newLayoutInfo ->
            try {
                val foldingFeature = newLayoutInfo.displayFeatures
                    .filterIsInstance<FoldingFeature>().firstOrNull()
                isLayoutUnfolded = (foldingFeature != null)
            } catch (e: Exception) {
                // If there was an issue detecting a foldable in the open position, default
                // to isLayoutUnfolded being false.
                isLayoutUnfolded = false
            }
        }
    }

    val viewFinderState by viewModel.viewFinderState.collectAsStateWithLifecycle()
    var rotation by remember { mutableStateOf(Surface.ROTATION_0) }

    DisposableEffect(lifecycleOwner, context) {
        val rotationProvider = RotationProvider(context)
        val rotationListener: (Int) -> Unit = { rotationValue: Int ->
            if (rotationValue != rotation) {
                surfaceProvider?.let { provider ->
                    viewModel.setTargetRotation(
                        rotationValue,
                    )
                }
            }
            rotation = rotationValue
        }

        rotationProvider.addListener(Dispatchers.Main.asExecutor(), rotationListener)

        onDispose {
            rotationProvider.removeListener(rotationListener)
        }
    }

    val onPreviewSurfaceProviderReady: (Preview.SurfaceProvider) -> Unit = {
        surfaceProvider = it
        viewModel.startPreview(lifecycleOwner, it, captureMode, cameraSelector, rotation)
    }

    fun setCaptureMode(mode: CaptureMode) {
        captureMode = mode
        surfaceProvider?.let { provider ->
            viewModel.startPreview(
                lifecycleOwner,
                provider,
                captureMode,
                cameraSelector,
                rotation,
            )
        }
    }

    fun setCameraSelector(selector: CameraSelector) {
        cameraSelector = selector
        surfaceProvider?.let { provider ->
            viewModel.startPreview(
                lifecycleOwner,
                provider,
                captureMode,
                cameraSelector,
                rotation,
            )
        }
    }

    @SuppressLint("MissingPermission")
    fun onVideoRecordingStart() {
        captureMode = CaptureMode.VIDEO_RECORDING
        viewModel.startVideoCapture(onMediaCaptured)
    }

    fun onVideoRecordingFinish() {
        captureMode = CaptureMode.VIDEO_READY
        viewModel.saveVideo()
    }

    if (cameraAndRecordAudioPermissionState.allPermissionsGranted) {
        Box(modifier = modifier.fillMaxSize().background(color = Color.Black)) {
            ViewFinder(
                viewFinderState.cameraState,
                onPreviewSurfaceProviderReady,
                viewModel::setZoomScale,
            )

            BackButton(
                modifier = Modifier.padding(16.dp),
                onMediaCaptured = onMediaCaptured,
                captureMode = captureMode,
                onPhotoButtonClick = { setCaptureMode(CaptureMode.PHOTO) },
                onVideoButtonClick = { setCaptureMode(CaptureMode.VIDEO_READY) },
                cameraSelector = cameraSelector,
                setCameraSelector = ::setCameraSelector,
                onPhotoCapture = { viewModel.capturePhoto(onMediaCaptured) },
                onVideoRecordingStart = { onVideoRecordingStart() },
                onVideoRecordingFinish = { onVideoRecordingFinish() },
                navigatePhotoPicker = navigatePhotoPicker
            )
        }
    } else {
        CameraAndRecordAudioPermission(cameraAndRecordAudioPermissionState) { onMediaCaptured(null) }
    }
}
