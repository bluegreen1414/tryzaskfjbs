package com.kapirti.social_chat_food_video.ui.presentation.home.explorephoto

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kapirti.social_chat_food_video.model.ExplorePhotoVideo
import kotlinx.coroutines.delay

@Composable
fun ExplorePhotoItem(
    modifier: Modifier = Modifier,
    media: ExplorePhotoVideo,
    markAsWatched: () -> Unit,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(media.uri)
            .build(),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Fit,
    )

    LaunchedEffect(true) {
        delay(1000L)
        markAsWatched()
    }
}
