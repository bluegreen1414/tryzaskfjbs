package com.kapirti.social_chat_food_video.ui.presentation.camera.common

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.kapirti.social_chat_food_video.common.composable.BackDoneToolbar
import com.kapirti.social_chat_food_video.ui.presentation.edit.question.PhotoDefaultImage

@Composable
fun PhotoScreen(
    title: String,
    isDoneBtnWorking: Boolean,
    imageUri: Uri?,
    popUp: () -> Unit,
    onPhotoTaken: (Uri) -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    Scaffold(
        topBar = {
            BackDoneToolbar(
                isDoneBtnWorking = isDoneBtnWorking,
                popUp = popUp,
                onDoneClick = onDoneClick,
                title = title
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            PhotoScreen(
                imageUri = imageUri,
                onPhotoTaken = onPhotoTaken,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun PhotoScreen(
    imageUri: Uri?,
    onPhotoTaken: (Uri) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasPhoto = imageUri != null
    val iconResource = if (hasPhoto) {
        Icons.Filled.SwapHoriz
    } else {
        Icons.Filled.AddAPhoto
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { success ->
            if (success != null) {
                onPhotoTaken(success)
            } else { }
        }
    )


    OutlinedButton(
        onClick = {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(),
        modifier = modifier
    ) {
        Column {
            if (hasPhoto) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(96.dp)
                        .aspectRatio(4 / 3f)
                )
            } else {
                PhotoDefaultImage(
                    modifier = Modifier.padding(
                        horizontal = 86.dp,
                        vertical = 74.dp
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.BottomCenter)
                    .padding(vertical = 26.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = iconResource,
                    contentDescription = stringResource(AppText.add_photo)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(
                        id = if (hasPhoto) {
                            AppText.retake_photo
                        } else {
                            AppText.add_photo
                        }
                    )
                )
            }
        }
    }
}
