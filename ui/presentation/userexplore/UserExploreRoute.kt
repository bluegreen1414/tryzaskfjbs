package com.kapirti.social_chat_food_video.ui.presentation.userexplore

import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kapirti.social_chat_food_video.common.composable.HomeBackground
import com.kapirti.social_chat_food_video.common.composable.KapirtiIcons
import com.kapirti.social_chat_food_video.R.string as AppText
import com.kapirti.social_chat_food_video.model.ExploreType
import com.kapirti.social_chat_food_video.model.ExploreUserItem
import kotlin.math.absoluteValue

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun UserExploreRoute(
    id: String,
    popUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserExploreViewModel = hiltViewModel()
) {
    LaunchedEffect(id) {
        viewModel.initialize(id)
    }

    val media = viewModel.media
    val player = viewModel.player
    val videoRatio = viewModel.videoRatio
    Scaffold(modifier = modifier) { contentPadding ->
        HomeBackground(modifier = Modifier.fillMaxSize())
        if (media.isEmpty()) { } else {
            TimelineVerticalPager(
                contentPadding,
                Modifier,
                popUp,
                media,
                player,
                viewModel::initializePlayer,
                viewModel::releasePlayer,
                viewModel::changePlayerItem,
                videoRatio,
            )
        }
    }
}

@Composable
fun TimelineVerticalPager(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    popUp: () -> Unit,
    mediaItems: List<ExploreUserItem>,
    player: Player?,
    onInitializePlayer: () -> Unit = {},
    onReleasePlayer: () -> Unit = {},
    onChangePlayerItem: (uri: Uri?, page: Int) -> Unit = { uri: Uri?, i: Int -> },
    videoRatio: Float?,
) {
    val pagerState = rememberPagerState(pageCount = { mediaItems.count() })
    LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the settledPage
        snapshotFlow { pagerState.settledPage }.collect { page ->
            if (mediaItems[page].type == ExploreType.VIDEO.toString()) {
                onChangePlayerItem(Uri.parse(mediaItems[page].uri), pagerState.currentPage)
            } else {
                onChangePlayerItem(null, pagerState.currentPage)
            }
        }
    }

    val currentOnInitializePlayer by rememberUpdatedState(onInitializePlayer)
    val currentOnReleasePlayer by rememberUpdatedState(onReleasePlayer)
    if (Build.VERSION.SDK_INT > 23) {
        LifecycleStartEffect(true) {
            currentOnInitializePlayer()
            onStopOrDispose {
                currentOnReleasePlayer()
            }
        }
    } else {
        LifecycleResumeEffect(true) {
            currentOnInitializePlayer()
            onPauseOrDispose {
                currentOnReleasePlayer()
            }
        }
    }

    VerticalPager(
        state = pagerState,
        modifier = modifier
            .padding(contentPadding)
            .fillMaxSize(),
    ) { page ->
        if (player != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .graphicsLayer {
                        val pageOffset = (
                            (pagerState.currentPage - page) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue

                        alpha = lerp(
                            start = 0f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f),
                        )
                    },
            ) {
                TimelinePage(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    media = mediaItems[page],
                    player = player,
                    page,
                    pagerState,
                    videoRatio,
                )

                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Column{
                        IconButton(modifier = Modifier.padding(bottom = 8.dp), onClick = popUp) {
                            Icon(
                                imageVector = KapirtiIcons.ArrowBack,
                                contentDescription = stringResource(AppText.cd_profile_photo),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimelinePage(
    modifier: Modifier = Modifier,
    media: ExploreUserItem,
    player: Player,
    page: Int,
    state: PagerState,
    videoRatio: Float?,
) {
    when (media.type) {
        ExploreType.VIDEO.toString() -> {
            if (page == state.settledPage) {
                if (LocalInspectionMode.current) {
                    Box(modifier = modifier)
                    return
                }
                AndroidView(
                    factory = { PlayerView(it) },
                    update = { playerView ->
                        playerView.player = player
                    },
                    modifier = modifier.fillMaxSize(),
                )
            }
        }
        ExploreType.PHOTO.toString() -> {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(media.uri)
                    .build(),
                contentDescription = null,
                modifier = modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
    }
}







//  onItemClicked = {
//  includeUserIdViewModel.addPartnerId(it.writerId)
// navigateUserProfile()
//},
//onSendClick = { timeline, text ->
//  viewModel.onSendClick(id = timeline.id, text = text)
//},
