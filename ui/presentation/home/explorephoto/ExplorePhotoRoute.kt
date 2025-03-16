package com.kapirti.social_chat_food_video.ui.presentation.home.explorephoto

import android.net.Uri
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.kapirti.social_chat_food_video.common.composable.DialogCancelButton
import com.kapirti.social_chat_food_video.common.composable.DialogConfirmButton
import com.kapirti.social_chat_food_video.common.composable.HomeBackground
import com.kapirti.social_chat_food_video.common.composable.NativeAdSmall
import com.kapirti.social_chat_food_video.model.ExplorePhotoVideo
import kotlin.math.absoluteValue

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun ExplorePhotoRoute(
    onPublisherClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExplorePhotoViewModel = hiltViewModel()
) {
    val media by viewModel.media.collectAsState()

    Scaffold(modifier = modifier) { contentPadding ->
        HomeBackground(modifier = Modifier.fillMaxSize())
        TimelineVerticalPager(
            contentPadding,
            Modifier,
            media,
            onReportClick = { viewModel.onReportClick(it) },
            onPublisherClick = onPublisherClick,
            markAsWatched = { mediaid -> viewModel.markAsWatched(mediaid) }
        )
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
    mediaItems: List<ExplorePhotoVideo>,
    onChangePlayerItem: (uri: Uri?, page: Int) -> Unit = { uri: Uri?, i: Int -> },
    onReportClick: (ExplorePhotoVideo) -> Unit,
    onPublisherClick: (String) -> Unit,
    markAsWatched: (String) -> Unit
) {
    val totalSize = mediaItems.size + mediaItems.size / 3
    val pagerState = rememberPagerState(pageCount = { totalSize })
    LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the settledPage
        snapshotFlow { pagerState.settledPage }.collect { page ->
            onChangePlayerItem(null, pagerState.currentPage)
        }
    }

    VerticalPager(
        state = pagerState,
        modifier = modifier
            .padding(contentPadding)
            .fillMaxSize(),
    ) { page ->
        if ((page + 1) % 4 == 0) {
            NativeAdSmall(modifier = Modifier.padding(vertical = 8.dp))
        } else {
            val actualIndex = page - (page / 4)
            if (actualIndex < mediaItems.size) {
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
                    ExplorePhotoItem(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        media = mediaItems[page],
                        markAsWatched = { markAsWatched(mediaItems[page].id) }
                    )

                    ExplorePhotoMetadataOverlay(
                        modifier = Modifier, mediaItem = mediaItems[page],
                        onReportClick = { onReportClick(mediaItems[page]) },
                        onPublisherClick = { onPublisherClick(mediaItems[page].userId) }
                    )
                }
            }
        }
    }
}
