package com.kapirti.social_chat_food_video.ui.presentation.camera

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.Display
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.DisplayOrientedMeteringPointFactory
import androidx.camera.core.DynamicRange
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.extensions.ExtensionMode
import androidx.camera.extensions.ExtensionsManager
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.compose.runtime.mutableStateOf
import androidx.concurrent.futures.await
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.kapirti.social_chat_food_video.core.datastore.CameraTypeRepository
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

private const val TAG = "CameraViewModel"

@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val cameraProviderManager: CameraXProcessCameraProviderManager,
    private val cameraTypeRepository: CameraTypeRepository,
    //private val chatViewModel: ChatViewModel,
    private val savedStateHandle: SavedStateHandle,
    logService: LogService
) : KapirtiViewModel(logService) {
    private lateinit var camera: Camera
    private lateinit var extensionsManager: ExtensionsManager
    private lateinit var videoCaptureUseCase: VideoCapture<Recorder>

    val chatId: String? = savedStateHandle.get("chatId")
    var viewFinderState = MutableStateFlow(ViewFinderState())

    val aspectRatioStrategy =
        AspectRatioStrategy(AspectRatio.RATIO_16_9, AspectRatioStrategy.FALLBACK_RULE_NONE)
    var resolutionSelector = ResolutionSelector.Builder()
        .setAspectRatioStrategy(aspectRatioStrategy)
        .build()

    private val previewUseCase = Preview.Builder()
        .setResolutionSelector(resolutionSelector)
        .build()

    private val imageCaptureUseCase = ImageCapture.Builder()
        .setResolutionSelector(resolutionSelector)
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()

    private val recorder = Recorder.Builder()
        .setAspectRatio(AspectRatio.RATIO_16_9)
        .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
        .build()

    private var currentRecording: Recording? = null
    private lateinit var recordingState: VideoRecordEvent

    private val _cameraState = mutableStateOf<String?>(null)
    val cameraState: String?
        get() = _cameraState.value

    init {
        val videoCaptureBuilder = VideoCapture.Builder(recorder)
        launchCatching {
            val hdrCameraInfo = getHdrCameraInfo()

            if (hdrCameraInfo != null) {
                Log.i(TAG, "Capturing HDR video")
                videoCaptureBuilder.setDynamicRange(hdrCameraInfo)
            }

            videoCaptureUseCase = videoCaptureBuilder.build()
            cameraTypeRepository.readState().collect{
                _cameraState.value = it
            }
        }
    }

    private suspend fun getHdrCameraInfo(): DynamicRange? {
        var supportedHdrEncoding: DynamicRange? = null

        cameraProviderManager.getCameraProvider().availableCameraInfos
            .first { cameraInfo ->
                val videoCapabilities = Recorder.getVideoCapabilities(cameraInfo)
                val supportedDynamicRanges =
                    videoCapabilities.supportedDynamicRanges

                supportedHdrEncoding = supportedDynamicRanges.firstOrNull {
                    // To ensure consistency between the multiple dynamic range profiles, chose
                    // HLG 10-bit, which is supported by all devices that supports HDR.
                    it == DynamicRange.HLG_10_BIT
                }
                return@first true
            }

        return supportedHdrEncoding
    }

    fun setChatId(chatId: Long) {
        savedStateHandle.set("chatId", chatId)
    }

    fun startPreview(
        lifecycleOwner: LifecycleOwner,
        surfaceProvider: Preview.SurfaceProvider,
        captureMode: CaptureMode,
        cameraSelector: CameraSelector,
        rotation: Int,
    ) {
        launchCatching {
            val cameraProvider = cameraProviderManager.getCameraProvider()
            val extensionManagerJob = launchCatching {
                extensionsManager = ExtensionsManager.getInstanceAsync(
                    application,
                    cameraProvider,
                ).await()
            }
            var extensionsCameraSelector: CameraSelector? = null
            val useCaseGroupBuilder = UseCaseGroup.Builder()

            previewUseCase.setSurfaceProvider(surfaceProvider)
            useCaseGroupBuilder.addUseCase(previewUseCase)

            if (captureMode == CaptureMode.PHOTO) {
                try {
                    extensionManagerJob.join()

                    // Query if extension is available.
                    if (extensionsManager.isExtensionAvailable(
                            cameraSelector,
                            ExtensionMode.NIGHT,
                        )
                    ) {
                        // Retrieve extension enabled camera selector
                        extensionsCameraSelector =
                            extensionsManager.getExtensionEnabledCameraSelector(
                                cameraSelector,
                                ExtensionMode.NIGHT,
                            )
                    }
                } catch (e: InterruptedException) {
                    // This should not happen unless the future is cancelled or the thread is
                    // interrupted by applications.
                }

                imageCaptureUseCase.targetRotation = rotation
                useCaseGroupBuilder.addUseCase(imageCaptureUseCase)
            } else if (captureMode == CaptureMode.VIDEO_READY || captureMode == CaptureMode.VIDEO_RECORDING) {
                videoCaptureUseCase.targetRotation = rotation
                useCaseGroupBuilder.addUseCase(videoCaptureUseCase)
            }

            cameraProvider.unbindAll()
            val activeCameraSelector = extensionsCameraSelector ?: cameraSelector
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                activeCameraSelector,
                useCaseGroupBuilder.build(),
            )
            viewFinderState.value.cameraState = CameraState.READY
        }
    }

    fun setTargetRotation(rotation: Int) {
        imageCaptureUseCase.targetRotation = rotation
        videoCaptureUseCase.targetRotation = rotation
    }

    fun capturePhoto(onMediaCaptured: (Media) -> Unit) {
        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/SociaLite")
            }
        }

        val context: Context = application
        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues,
            )
            .build()
        imageCaptureUseCase.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    val msg = "Photo capture failed."
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri
                    if (savedUri != null) {
                        sendPhotoMessage(savedUri.toString())
                        onMediaCaptured(Media(savedUri, MediaType.PHOTO))
                    } else {
                        val msg = "Photo capture failed."
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            },
        )
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startVideoCapture(onMediaCaptured: (Media) -> Unit) {
        val name = "Socialite-recording-" +
            SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".mp4"
        val contentValues = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/SociaLite")
            }
        }
        val context: Context = application
        val mediaStoreOutput = MediaStoreOutputOptions.Builder(
            context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        )
            .setContentValues(contentValues)
            .build()

        val captureListener = Consumer<VideoRecordEvent> { event ->
            recordingState = event
            if (event is VideoRecordEvent.Finalize) {
                onMediaCaptured(Media(event.outputResults.outputUri, MediaType.VIDEO))
            }
        }

        // configure Recorder and Start recording to the mediaStoreOutput.
        currentRecording = videoCaptureUseCase.output
            .prepareRecording(context, mediaStoreOutput)
            .apply { withAudioEnabled() }
            .start(ContextCompat.getMainExecutor(context), captureListener)
    }

    fun sendPhotoMessage(photoUri: String) {
        launchCatching {
            if (chatId != null) {
//                repository.sendMessage(
//                    chatId = chatId,
//                    text = "",
//                    mediaUri = photoUri,
//                    mediaMimeType = "image/jpeg",
//                )
//                chatViewModel.sendMessageToFCMserver(
//                    chatId = chatId,
//                    text = "",
//                    mediaUri = photoUri,
//                    mediaMimeType = "image/jpeg",
//                )
                //chatViewModel.sendPhoto(chatId, photoUri)
            }
        }
    }

    fun saveVideo() {
        if (currentRecording == null || recordingState is VideoRecordEvent.Finalize) {
            return
        }

        val recording = currentRecording
        if (recording != null) {
            recording.stop()
            currentRecording = null
        }
    }

    companion object {
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        val aspectRatios = mapOf(AspectRatio.RATIO_16_9 to (9.0 / 16.0).toFloat())
    }

    fun tapToFocus(
        display: Display,
        surfaceWidth: Int,
        surfaceHeight: Int,
        x: Float,
        y: Float,
    ) {
        camera?.let { camera ->
            val meteringPoint =
                DisplayOrientedMeteringPointFactory(
                    display,
                    camera.cameraInfo,
                    surfaceWidth.toFloat(),
                    surfaceHeight.toFloat(),
                ).createPoint(x, y)

            val action = FocusMeteringAction.Builder(meteringPoint).build()

            camera.cameraControl.startFocusAndMetering(action)
        }
    }

    fun setZoomScale(scale: Float) {
        val zoomState = camera?.cameraInfo?.zoomState?.value
        if (zoomState == null) return
        val finalScale =
            (zoomState.zoomRatio * scale).coerceIn(
                zoomState.minZoomRatio,
                zoomState.maxZoomRatio,
            )
        camera?.cameraControl?.setZoomRatio(finalScale)
    }
}

data class ViewFinderState(
    var cameraState: CameraState = CameraState.NOT_READY,
    val lensFacing: Int = CameraSelector.LENS_FACING_BACK,
)

enum class CameraState {
    NOT_READY,
    READY,
    PREVIEW_STOPPED,
}

enum class CaptureMode {
    PHOTO,
    VIDEO_READY,
    VIDEO_RECORDING,
}
