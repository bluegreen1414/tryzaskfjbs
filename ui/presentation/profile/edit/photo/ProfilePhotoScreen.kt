package com.kapirti.social_chat_food_video.ui.presentation.profile.edit.photo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.kapirti.social_chat_food_video.R
import com.kapirti.social_chat_food_video.ui.presentation.camera.common.PhotoScreen

@Composable
fun ProfilePhotoScreen(
    popUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfilePhotoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    PhotoScreen(
        isDoneBtnWorking = uiState.isDoneBtnWorking,
        popUp = popUp,
        onDoneClick = { viewModel.savePhoto(popUp) },
        imageUri = viewModel.selfieUri,
        onPhotoTaken = viewModel::onSelfieResponse,
        title = stringResource(R.string.cd_profile_photo)
    )
}
