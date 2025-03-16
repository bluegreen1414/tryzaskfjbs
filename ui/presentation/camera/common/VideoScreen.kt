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
import com.kapirti.social_chat_food_video.R.string as AppText
import com.kapirti.social_chat_food_video.R.drawable as AppIcon
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import android.graphics.Bitmap
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Scaffold
import com.kapirti.social_chat_food_video.common.composable.BackDoneToolbar

@Composable
fun VideoScreen (
    @StringRes title: Int,
    isDoneBtnWorking: Boolean,
    popUp: () -> Unit,
    videoUri: Uri?,
    onVideoTaken: (Uri?) -> Unit,
    thumbnail: Bitmap?,
    onThumbnailTaken: (Bitmap?) -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {

        }
    ) { innerPadding ->
        VideoScreen(
            videoUri = videoUri,
            onVideoTaken = onVideoTaken,
            thumbnail = thumbnail,
            onThumbnailTaken = onThumbnailTaken,
            modifier = modifier.padding(innerPadding),
        )
    }
}


@Composable
private fun VideoScreen(
    videoUri: Uri?,
    onVideoTaken: (Uri?) -> Unit,
    thumbnail: Bitmap?,
    onThumbnailTaken: (Bitmap?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasVideo = videoUri != null
    val iconResource = if (hasVideo) {
        Icons.Filled.SwapHoriz
    } else {
        Icons.Filled.AddAPhoto
    }

    val singleVideoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { success ->
            if (success != null) {
                onVideoTaken(success)
            } else { }
        }
    )
    val context = LocalContext.current


    OutlinedButton(
        onClick = {
            singleVideoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
            )
        },
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(),
        modifier = modifier
    ) {
        Column {
            if (hasVideo) {
                videoUri?.let {
/**                    LaunchedEffect(videoUri) {
                        getVideoThumbnail(videoUri, context) { bitmap ->
                            onThumbnailTaken(bitmap)
                        }
                    }*/
                    thumbnail?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Video Thumbnail",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f) // Kare görsel
                                .padding(4.dp)
                        )
                    }
                }
            } else {
                VideoDefaultImage(
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
                    contentDescription = stringResource(AppText.add_video)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(
                        id = if (hasVideo) {
                            AppText.retake_video
                        } else {
                            AppText.add_video
                        }
                    )
                )
            }
        }
    }
}

/**
private fun getVideoThumbnail(videoUri: Uri, context: Context, onThumbnailReady: (Bitmap) -> Unit) {
    Glide.with(context)
        .asBitmap()
        .load(videoUri)
        .apply(RequestOptions().frame(1000000)) // 1 saniye sonra kare al
        .transition(BitmapTransitionOptions.withCrossFade())
        .into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                onThumbnailReady(resource)
            }
        })
}
*/
@Composable
private fun VideoDefaultImage(
    modifier: Modifier = Modifier,
    lightTheme: Boolean = LocalContentColor.current.luminance() < 0.5f,
) {
    val assetId = if (lightTheme) {
        AppIcon.ic_selfie_light
    } else {
        AppIcon.ic_selfie_dark
    }
    Image(
        painter = painterResource(id = assetId),
        modifier = modifier,
        contentDescription = stringResource(AppText.add_video)
    )
}

/**


result.value?.let { image ->
Text(text = "Video Path: "+image.path.toString())
}
}

 */
