package com.kapirti.social_chat_food_video.ui.presentation.camera

import androidx.camera.core.CameraSelector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kapirti.social_chat_food_video.common.composable.KapirtiIcons
import kotlin.reflect.KFunction1

@Composable
fun BackButton(
    modifier: Modifier, navigatePhotoPicker: () -> Unit, onMediaCaptured: (Media?) -> Unit,
    captureMode: CaptureMode, onPhotoButtonClick: () -> Unit, onVideoButtonClick: () -> Unit,
    cameraSelector: CameraSelector, setCameraSelector: KFunction1<CameraSelector, Unit>,
    onPhotoCapture: () -> Unit, onVideoRecordingStart: () -> Unit, onVideoRecordingFinish: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(999f),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    onMediaCaptured(null)
                }
            ) {
                Icon(
                    imageVector = KapirtiIcons.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) { CameraSwitcher(captureMode, cameraSelector, setCameraSelector) }


        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { navigatePhotoPicker() }) {
                Icon(
                    imageVector = KapirtiIcons.PhotoPicker,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }


        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Column() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) { CameraControls(captureMode, onPhotoButtonClick, onVideoButtonClick) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) { ShutterButton(captureMode, onPhotoCapture, onVideoRecordingStart, onVideoRecordingFinish) }
            }
        }
    }
}

@Composable
private fun CameraControls(captureMode: CaptureMode, onPhotoButtonClick: () -> Unit, onVideoButtonClick: () -> Unit) {
    val activeButtonColor =
        ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    val inactiveButtonColor =
        ButtonDefaults.buttonColors(containerColor = Color.LightGray)
    if (captureMode != CaptureMode.VIDEO_RECORDING) {
        Button(
            modifier = Modifier.padding(5.dp),
            onClick = onPhotoButtonClick,
            colors = if (captureMode == CaptureMode.PHOTO) activeButtonColor else inactiveButtonColor,
        ) {
            Text("Photo")
        }
        Button(
            modifier = Modifier.padding(5.dp),
            onClick = onVideoButtonClick,
            colors = if (captureMode != CaptureMode.PHOTO) activeButtonColor else inactiveButtonColor,
        ) {
            Text("Video")
        }
    }
}

@Composable
private fun ShutterButton(captureMode: CaptureMode, onPhotoCapture: () -> Unit, onVideoRecordingStart: () -> Unit, onVideoRecordingFinish: () -> Unit) {
    Box(modifier = Modifier.padding(25.dp, 0.dp)) {
        if (captureMode == CaptureMode.PHOTO) {
            Button(
                onClick = onPhotoCapture,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .height(75.dp)
                    .width(75.dp),
            ) {}
        } else if (captureMode == CaptureMode.VIDEO_READY) {
            Button(
                onClick = onVideoRecordingStart,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .height(75.dp)
                    .width(75.dp),
            ) {}
        } else if (captureMode == CaptureMode.VIDEO_RECORDING) {
            Button(
                onClick = onVideoRecordingFinish,
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp),
            ) {}
            Spacer(modifier = Modifier.width(100.dp))
        }
    }
}

@Composable
private fun CameraSwitcher(captureMode: CaptureMode, cameraSelector: CameraSelector, setCameraSelector: KFunction1<CameraSelector, Unit>) {
    if (captureMode != CaptureMode.VIDEO_RECORDING) {
        IconButton(onClick = {
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                setCameraSelector(CameraSelector.DEFAULT_FRONT_CAMERA)
            } else {
                setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA)
            }
        }) {
            Icon(
                imageVector = Icons.Default.Autorenew,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .height(75.dp)
                    .width(75.dp),
            )
        }
    }
}
