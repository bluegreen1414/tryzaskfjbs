package com.kapirti.social_chat_food_video.ui.presentation.camera.photopicker.navigation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.kapirti.social_chat_food_video.ui.presentation.edit.SurveyQuestion
import com.kapirti.social_chat_food_video.ui.presentation.camera.photopicker.PhotoPickerScreen
import com.kapirti.social_chat_food_video.ui.presentation.camera.photopicker.PhotoPickerViewModel

@Composable
fun PhotoPickerRoute(
    viewModel: PhotoPickerViewModel = hiltViewModel(),
    onPhotoPicked: () -> Unit,
) {
    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri: Uri? ->
                if (uri != null) {
                    viewModel.onSelfieResponse(uri)
                   // viewModel.onPhotoPicked(uri,onPhotoPicked)
                }
            },
        )
    LaunchedEffect(Unit) {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo),
        )
    }

    val uiState by viewModel.uiState

    PhotoPickerScreen(
        cameraType = viewModel.cameraType ?: "",
        isError = uiState.isErrorText,
        value = uiState.text,
        onValueChange = viewModel::onTextChange,
        isDoneBtnWorking = uiState.isDoneBtnWorking,
        imageUri = viewModel.selfieUri,
        popUp = onPhotoPicked,
        save = { viewModel.save(onPhotoPicked)},
        onClick = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
            )
        }
    )

    if(viewModel.uiState.value.isErrorPhotos){
        LaunchedEffect(Unit) {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo),
            )
        }
    }
}
