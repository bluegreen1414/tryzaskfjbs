package com.kapirti.social_chat_food_video.ui.presentation.home.explore

import android.net.Uri
import android.os.Build
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.kapirti.social_chat_food_video.common.composable.DialogCancelButton
import com.kapirti.social_chat_food_video.common.composable.DialogConfirmButton
import com.kapirti.social_chat_food_video.common.composable.HomeBackground
import com.kapirti.social_chat_food_video.common.composable.NativeAdSmall
import com.kapirti.social_chat_food_video.model.ExplorePhotoVideo
import kotlin.math.absoluteValue
import kotlinx.coroutines.delay

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun ExploreRoute(
    restartAppExplore: () -> Unit,
    onPublisherClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ExploreViewModel = hiltViewModel()
    val media by viewModel.media.collectAsState()
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
                onInitializePlayer = {
                    viewModel.initializePlayer(restartAppExplore)

                },
                //viewModel::initializePlayer,
                viewModel::releasePlayer,
                viewModel::changePlayerItem,
                videoRatio,
                onReportClick = {
                    viewModel.onReportClick(it)
                },
                onPublisherClick = onPublisherClick,
                markAsWatched = { mediaid -> viewModel.markAsWatched(mediaid) }
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
    mediaItems: List<ExplorePhotoVideo>,
    player: Player?,
    onInitializePlayer: () -> Unit = {},
    onReleasePlayer: () -> Unit = {},
    onChangePlayerItem: (uri: Uri?, page: Int) -> Unit = { uri: Uri?, i: Int -> },
    videoRatio: Float?,
    onReportClick: (ExplorePhotoVideo) -> Unit,
    onPublisherClick: (String) -> Unit,
    markAsWatched: (String) -> Unit
) {
    val totalSize = mediaItems.size + mediaItems.size / 3
    val pagerState = rememberPagerState(pageCount = { totalSize })
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            onChangePlayerItem(Uri.parse(mediaItems[page].uri), pagerState.currentPage)
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
        if ((page + 1) % 4 == 0) {
            NativeAdSmall(modifier = Modifier.padding(vertical = 8.dp))
        } else {
            val actualIndex = page - (page / 4)
            if (actualIndex < mediaItems.size) {
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
                                .fillMaxSize()
                                .align(Alignment.Center),
                            media = mediaItems[page],
                            player = player,
                            page,
                            pagerState,
                            videoRatio,
                            markAsWatched = {markAsWatched(mediaItems[page].id)}
                        )

                        MetadataOverlay(
                            modifier = Modifier, mediaItem = mediaItems[page],
                            onReportClick = { onReportClick(mediaItems[page]) },
                            onPublisherClick = { onPublisherClick(mediaItems[page].userId) }
                        )
                        //onAddClick = onAddClick, onPublisherClick = onPublisherClick)
                    }
                }
            }
        }
    }
}

@Composable
fun TimelinePage(
    modifier: Modifier = Modifier,
    media: ExplorePhotoVideo,
    player: Player,
    page: Int,
    state: PagerState,
    videoRatio: Float?,
    markAsWatched: () -> Unit,
) {
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

    LaunchedEffect(true) {
        delay(3000L)
        markAsWatched()
    }
}










              //  onItemClicked = {
                  //  includeUserIdViewModel.addPartnerId(it.writerId)
                   // navigateUserProfile()
                //},
                //onSendClick = { timeline, text ->
                  //  viewModel.onSendClick(id = timeline.id, text = text)
                //},

               /** onBookmarkClick = {

                },
                onFavoriteClicked = {
                    if (uiState.isAnonymousAccount) {
                        navigateLogin()
                    } else {
                        viewModel.onFavoriteClicked(
                            navigateLike = navigateLike)
                    }
                }*/


/**
data class YouTubeShortsVideo(
    val id: String,
    val thumbnailUrl: String,
    val videoTitle: String,
    val channelName: String,
    val views: Int,
    val duration: Int // in seconds
)

@Composable
fun TimelineRoute(
    openDrawer: () -> Unit,
    isExpandedScreen: Boolean,
    navigateLogin: () -> Unit,
    navigateEdit: () -> Unit,
    // navigateUserProfile: () -> Unit,
    // navigateLike: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TimelineViewModel = hiltViewModel(),
){
    val timelines by viewModel.timelines.collectAsStateWithLifecycle()
/**    val videos = remember { // Sample data
        listOf(
            YouTubeShortsVideo("1", "thumbnail_url_1", "Video Title 1", "Channel 1", 1234, 60),
            // ... more videos
        )
    }*/

    LazyColumn {
        items(timelines) { video ->
            YouTubeShortsItem(video) { videoId ->
                // Handle video click (e.g., navigate to video player)
            }
        }
    }
}


@Composable
fun YouTubeShortsItem(video: Timeline, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(video.id) } // Handle click
    ) {
        AsyncImage(
            model = video.uri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f) // Maintain aspect ratio
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = video.videoTitle, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "video.channelName", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "${"video.views"} views", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.weight(1f)) // Push duration to the end
            Text(text = formatDuration(1578/**video.duration*/), style = MaterialTheme.typography.bodySmall)
        }
    }
}

fun formatDuration(durationInSeconds: Int): String {
    val minutes = durationInSeconds / 60
    val seconds = durationInSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}




    val uiState by viewModel.uiState.collectAsState(initial = SettingsUiState(false))

    Scaffold(
       /** topBar = {
            TimelineToolbar(
                openDrawer = openDrawer,
                isExpandedScreen = isExpandedScreen,
                onActionsClick = {
                    if (!uiState.isAnonymousAccount) {
                        viewModel.onAddClick(navigateEdit)
                    } else {
                        navigateLogin()
                    }
                }
            )
        },
        bottomBar = { AdsBannerToolbar(ads = ADS_TIMELINE_BANNER_ID) },*/
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->

                //  onItemClicked = {
                //  includeUserIdViewModel.addPartnerId(it.writerId)
                // navigateUserProfile()
                //},
                //onSendClick = { timeline, text ->
                //  viewModel.onSendClick(id = timeline.id, text = text)
                //},

                /** onBookmarkClick = {

                },
                onFavoriteClicked = {
                if (uiState.isAnonymousAccount) {
                navigateLogin()
                } else {
                viewModel.onFavoriteClicked(
                navigateLike = navigateLike)
                }
                }
            )
        }
    }



                MetadataOverlay(modifier = Modifier.padding(16.dp),
                    mediaItem = mediaItems[page],
                    onReportClick = onReportClick,
                    //timelineComments = mediaItems[page].comments,
                    //  onItemClicked = { onItemClicked(mediaItems[page]) },
                    // onSendClick = { onSendClick(mediaItems[page], it) }
                    /**  onFavoriyeClick = {
                    onFavoriteClicked(mediaItems[page])
                    },
                    onBookmarkClick = {
                    onBookmarkClick(mediaItems[page])
                    }*/
                )
            }
        }
    }


}
*/
*/


/**
 *
 *
 * @OptIn(ExperimentalMaterial3Api::class)
 * @Composable
 * private fun CraneHomeContent(
 *     includeCafeViewModel: IncludeCafeViewModel,
 *     includeRestaurantViewModel: IncludeRestaurantViewModel,
 *     includeHotelViewModel: IncludeHotelViewModel,
 *     navigateCafe: () -> Unit,
 *     navigateRestaurant: () -> Unit,
 *     navigateHotel: () -> Unit,
 *     navigateChats: () -> Unit,
 *     navigateEdit: () -> Unit,
 *     onExploreItemClicked: OnExploreItemClicked,
 *     onDateSelectionClicked: () -> Unit,
 *     modifier: Modifier = Modifier,
 *     viewModel: TourismViewModel = hiltViewModel()
 * ) {
 *     val scope = rememberCoroutineScope()
 *     val scaffoldState = rememberBottomSheetScaffoldState()
 *
 *      val mePhoto by viewModel.mePhoto.collectAsStateWithLifecycle()
 *     val stories by viewModel.stories.collectAsStateWithLifecycle()
 * //    val suggestedDestinations by viewModel.suggestedDestinations.observeAsState()
 *
 * //  val onPeopleChanged: (Int) -> Unit = { viewModel.updatePeople(it) }
 *     var tabSelected by remember { mutableStateOf(CraneScreenTourism.Cafes) }
 *     var activeId by rememberSaveable { mutableStateOf<String?>(null) }
 *     val scrim = remember(activeId) { FocusRequester() }
 *     var photoStock by remember { mutableStateOf<String?>(null) }
 *
 *
 *     BottomSheetScaffold(
 *         modifier = modifier,
 *         topBar = { HomeTabBar(tabSelected, onTabSelected = { tabSelected = it }, navigateChats) },
 *         scaffoldState = scaffoldState,
 *         sheetPeekHeight = viewModel.sheetPeekHeight.dp,
 *         //sheetPeekHeight = 128.dp,
 *         sheetContent = {
 *             when (tabSelected) {
 *                 CraneScreenTourism.Cafes -> {
 *                     ExploreSection(
 *                         exploreList = viewModel.galleries,
 *                         onCloseClick = viewModel::onSheetPeekClose,
 *                         onProfileClick = {
 *                             includeCafeViewModel.addCafe(viewModel.cafe ?: Cafe())
 *                             navigateCafe()
 *                         }
 *                         //exploreList = viewModel.hotels,
 *                         //onItemClicked = onExploreItemClicked
 *                     )
 *                 }
 *                 CraneScreenTourism.Restaurants -> {
 *                     ExploreSection(
 *                         exploreList = viewModel.galleries,
 *                         onCloseClick = viewModel::onSheetPeekClose,
 *                         onProfileClick = {
 *                             includeRestaurantViewModel.addRestaurant(viewModel.restaurant ?: Restaurant())
 *                             navigateRestaurant()
 *                         },
 *                         //exploreList = viewModel.hotels,
 *                         //onItemClicked = onExploreItemClicked
 *                     )
 *                 }
 *                 CraneScreenTourism.Hotels -> {
 *                     ExploreSection(
 *                         exploreList = viewModel.galleries,
 *                         onCloseClick = viewModel::onSheetPeekClose,
 *                         onProfileClick = {
 *                             includeHotelViewModel.addHotel(viewModel.hotel ?: Hotel())
 *                             navigateHotel()
 *                         }
 *                         //exploreList = viewModel.hotels,
 *                         //onItemClicked = onExploreItemClicked
 *                     )
 *                 }
 *             }
 *         }
 *     ) { innerPadding ->
 *         HomeBackground(modifier = Modifier.fillMaxSize())
 *
 *         Column {
 *             StoryContent(
 *                 mePhoto = mePhoto,
 *                 stories = stories,
 *                 onAddStoryClicked = { viewModel.onAddStoryClicked(navigateEdit) },
 *                 onWatchStoryClicked = {
 *                     photoStock = it.contentUrl
 *                     activeId = it.contentUrl
 *                 }
 *             )
 *             SearchContent(
 *                 tabSelected,
 *                 cafes,
 *                 restaurants,
 *                 hotels,
 *                 modifier = modifier.padding(innerPadding),
 *             )
 *         }
 *     }
 * }
 *
 */
