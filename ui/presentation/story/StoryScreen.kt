package com.kapirti.social_chat_food_video.ui.presentation.story

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kapirti.social_chat_food_video.common.composable.DialogCancelButton
import com.kapirti.social_chat_food_video.common.composable.DialogConfirmButton
import com.kapirti.social_chat_food_video.common.composable.HomeBackground
import com.kapirti.social_chat_food_video.common.composable.ReportButton
import com.kapirti.social_chat_food_video.model.ExploreType
import kotlin.math.absoluteValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun StoryScreen(
    id: String,
    modifier: Modifier = Modifier,
    viewModel: StoryViewModel = hiltViewModel()
) {

    LaunchedEffect(id) {
        viewModel.getStories(id)
    }

    val media = viewModel.media
    val player = viewModel.player
    val videoRatio = viewModel.videoRatio
    Scaffold(modifier = modifier) { contentPadding ->
        HomeBackground(modifier = Modifier.fillMaxSize())
        if (media.isEmpty()) {
            //EmptyTimeline(contentPadding, modifier)
        } else {
            TimelineVerticalPager(
                contentPadding,
                Modifier,
                media,
                player,
                viewModel::initializePlayer,
                viewModel::releasePlayer,
                viewModel::changePlayerItem,
                videoRatio,
                onReportClick = {
                    viewModel.onReportClick(it)
                },
            )
        }
    }
    if (viewModel.showReportDialog.value) {
        AlertDialog(
            title = { Text(stringResource(AppText.report)) },
            text = { Text(stringResource(AppText.report_user)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { viewModel.showReportDialog.value = false } },
            confirmButton = { DialogConfirmButton(AppText.report) { viewModel.onReportButtonClick() } },
            onDismissRequest = { viewModel.showReportDialog.value = false }
        )
    }
    if (viewModel.showReportDone.value) {
        AlertDialog(
            title = { Text(stringResource(AppText.thank_you)) },
            text = { Text(stringResource(AppText.report_done_text)) },
            confirmButton = { DialogConfirmButton(AppText.ok) { viewModel.onReportDoneDismiss() } },
            onDismissRequest = { viewModel.onReportDoneDismiss() }
        )
    }
}

@Composable
private fun TimelineVerticalPager(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    mediaItems: List<StoryItem>,
    player: Player?,
    onInitializePlayer: () -> Unit = {},
    onReleasePlayer: () -> Unit = {},
    onChangePlayerItem: (uri: Uri?, page: Int) -> Unit = { uri: Uri?, i: Int -> },
    videoRatio: Float?,
    onReportClick: (StoryItem) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { mediaItems.count() })
    val coroutineScope = rememberCoroutineScope()
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



    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    when {
                        dragAmount > 100 -> { // Sağa kaydırınca önceki hikaye
                            if (pagerState.currentPage > 0) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            }
                        }

                        dragAmount < -100 -> { // Sola kaydırınca sonraki hikaye
                            if (pagerState.currentPage < mediaItems.size - 1) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else {
                              //  onClose() // Son hikaye ise çık
                            }
                        }
                    }
                }
            }
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

                        // We animate the alpha, between 0% and 100%
                        alpha = lerp(
                            start = 0f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f),
                        )
                    },
            ) {
                TimelinePage(
                    modifier = Modifier
                        .align(Alignment.Center),
                    media = mediaItems[page],
                    player = player,
                    page,
                    pagerState,
                    videoRatio,
                )

                MetadataOverlay(modifier = Modifier.padding(16.dp), mediaItem = mediaItems[page])
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Column{
                        /**                        IconButton(modifier = Modifier.padding(bottom = 8.dp), onClick = {}){
                        Icon(
                        imageVector = LuccaIcons.Menu,
                        contentDescription = stringResource(AppText.cd_profile_photo),
                        modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                        )
                        }*/
                        /**                        IconButton(modifier = Modifier.padding(bottom = 8.dp), onClick = {}){
                        Icon(
                        imageVector = LuccaIcons.Menu,
                        contentDescription = stringResource(AppText.cd_profile_photo),
                        modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                        )
                        }*/
                        ReportButton(onClick = {onReportClick(mediaItems[page])})
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimelinePage(
    modifier: Modifier = Modifier,
    media: StoryItem,
    player: Player,
    page: Int,
    state: PagerState,
    videoRatio: Float?,
) {
    when (media.type) {
        ExploreType.VIDEO.toString() -> {
            if (page == state.settledPage) {
                // When in preview, early return a Box with the received modifier preserving layout
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
                modifier = modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Composable
fun MetadataOverlay(modifier: Modifier, mediaItem: StoryItem) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(999f),
    ) {
        if (mediaItem.type == ExploreType.VIDEO.toString()) {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            val context = LocalContext.current.applicationContext

            // Running on an IO thread for loading metadata from remote urls to reduce lag time
            val duration: State<Long?> = produceState<Long?>(initialValue = null) {
                withContext(Dispatchers.IO) {
                    // Remote url
                    if (mediaItem.uri.contains("https://")) {
                        mediaMetadataRetriever.setDataSource(mediaItem.uri, HashMap<String, String>())
                    } else { // Locally saved files
                        mediaMetadataRetriever.setDataSource(context, Uri.parse(mediaItem.uri))
                    }
                    value =
                        mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                            ?.toLong()
                }
            }
            duration.value?.let {
                val seconds = it / 1000L
                val minutes = seconds / 60L
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "%d:%02d".format(minutes, seconds % 60),
                    )
                }
            }
        }
    }
}
