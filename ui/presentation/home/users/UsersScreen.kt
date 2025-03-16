package com.kapirti.social_chat_food_video.ui.presentation.home.users

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.kapirti.social_chat_food_video.common.composable.NotificationPermissionCard
import com.kapirti.social_chat_food_video.model.UserFlirt
import com.kapirti.social_chat_food_video.model.UserLP
import com.kapirti.social_chat_food_video.model.UserMarriage
import com.kapirti.social_chat_food_video.ui.presentation.story.Story
import com.kapirti.social_chat_food_video.ui.presentation.story.StoryRoute
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kapirti.social_chat_food_video.common.composable.NativeAdSmall
import com.kapirti.social_chat_food_video.model.Cafe
import com.kapirti.social_chat_food_video.model.Hotel
import com.kapirti.social_chat_food_video.model.Restaurant



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FlirtContent(
    users: List<UserFlirt>,
    onItemClicked: (UserFlirt) -> Unit,
    myId: String,
    myStory: Story?,
    myName: String,
    myPhoto: String,
    stories: List<Story>,
    onCreateStoryClick: () -> Unit,
    onStoryWatchClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    @SuppressLint("InlinedApi") // Granted at install time on API <33.
    val notificationPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS,
    )
    LazyColumn(
        modifier = modifier//.LazyColumnPadding(),
    ) {
        val totalSize = users.size + users.size / 3

        if (!notificationPermissionState.status.isGranted) {
            item {
                NotificationPermissionCard(
                    shouldShowRationale = notificationPermissionState.status.shouldShowRationale,
                    onGrantClick = {
                        notificationPermissionState.launchPermissionRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
        }
        item {
            StoryRoute(
                myId = myId,
                myName = myName,
                myPhoto = myPhoto,
                stories = stories,
                myItem = myStory,
                onCreateStoryClick = onCreateStoryClick,
                onStoryWatchClick = onStoryWatchClick
            )
        }
        items(totalSize) { archive ->
            if ((archive + 1) % 4 == 0) {
                NativeAdSmall(modifier = Modifier.padding(vertical = 8.dp))
            } else {
                val actualIndex = archive - (archive / 4)
                if (actualIndex < users.size) {
                    UserItem(
                        photo = users[actualIndex].photo,
                        nameSurname = users[actualIndex].username,
                        description = users[actualIndex].description,
                        online = users[actualIndex].online,
                        lastSeen = users[actualIndex].lastSeen,
                        navigateToAccountDetail = { onItemClicked(users[actualIndex]) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MarriageContent(
    users: List<UserMarriage>,
    onItemClicked: (UserMarriage) -> Unit,
    myId: String,
    myName: String,
    myPhoto: String,
    stories: List<Story>,
    myStory: Story?,
    onCreateStoryClick: () -> Unit,
    onStoryWatchClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    @SuppressLint("InlinedApi") // Granted at install time on API <33.
    val notificationPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS,
    )
    LazyColumn(
        modifier = modifier//.LazyColumnPadding(),
    ){
        val totalSize = users.size + users.size / 3
        if (!notificationPermissionState.status.isGranted) {
            item {
                NotificationPermissionCard(
                    shouldShowRationale = notificationPermissionState.status.shouldShowRationale,
                    onGrantClick = {
                        notificationPermissionState.launchPermissionRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
        }
        item {
            StoryRoute(
                myId = myId,
                myName = myName,
                myPhoto = myPhoto,
                stories = stories,
                myItem = myStory,
                onCreateStoryClick = onCreateStoryClick,
                onStoryWatchClick = onStoryWatchClick
            )
        }
        items(totalSize) { archive ->
            if ((archive + 1) % 4 == 0) {
                NativeAdSmall(modifier = Modifier.padding(vertical = 8.dp))
            } else {
                val actualIndex = archive - (archive / 4)
                if (actualIndex < users.size) {
                    UserItem(
                        photo = users[actualIndex].photo,
                        nameSurname = users[actualIndex].username,
                        description = users[actualIndex].description,
                        online = users[actualIndex].online,
                        lastSeen = users[actualIndex].lastSeen,
                        navigateToAccountDetail = { onItemClicked(users[actualIndex]) }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LanguageContent(
    users: List<UserLP>,
    onItemClicked: (UserLP) -> Unit,
    myId: String,
    myName: String,
    myPhoto: String,
    myStory: Story?,
    stories: List<Story>,
    onCreateStoryClick: () -> Unit,
    onStoryWatchClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    @SuppressLint("InlinedApi")
    val notificationPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS,
    )

    LazyColumn(modifier = modifier){
        val totalSize = users.size + users.size / 3
        if (!notificationPermissionState.status.isGranted) {
            item {
                NotificationPermissionCard(
                    shouldShowRationale = notificationPermissionState.status.shouldShowRationale,
                    onGrantClick = {
                        notificationPermissionState.launchPermissionRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
        }
        item {
            StoryRoute(
                myId = myId,
                myName = myName,
                myPhoto = myPhoto,
                stories = stories,
                myItem = myStory,
                onCreateStoryClick = onCreateStoryClick,
                onStoryWatchClick = onStoryWatchClick
            )
        }
        items(totalSize) { archive ->
            if ((archive + 1) % 4 == 0) {
                NativeAdSmall(modifier = Modifier.padding(vertical = 8.dp))
            } else {
                val actualIndex = archive - (archive / 4)
                if (actualIndex < users.size) {
                    UserItem(
                        photo = users[actualIndex].photo,
                        nameSurname = users[actualIndex].username,
                        description = users[actualIndex].description,
                        online = users[actualIndex].online,
                        lastSeen = users[actualIndex].lastSeen,
                        navigateToAccountDetail = { onItemClicked(users[actualIndex]) }
                    )
                }
            }
        }
    }
}

@Composable
fun CafesContent(
    users: List<Cafe>,
    onItemClicked: (Cafe) -> Unit,
    myId: String,
    myName: String,
    myPhoto: String,
    stories: List<Story>,
    myStory: Story?,
    onCreateStoryClick: () -> Unit,
    onStoryWatchClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ){
        val totalSize = users.size + users.size / 3
        item {
            StoryRoute(
                myId = myId,
                myName = myName,
                myPhoto = myPhoto,
                stories = stories,
                myItem = myStory,
                onCreateStoryClick = onCreateStoryClick,
                onStoryWatchClick = onStoryWatchClick
            )
        }
        items(totalSize) { archive ->
            if ((archive + 1) % 4 == 0) {
                NativeAdSmall(modifier = Modifier.padding(vertical = 8.dp))
            } else {
                val actualIndex = archive - (archive / 4)
                if (actualIndex < users.size) {
                    UserItem(
                        photo = users[actualIndex].photo,
                        nameSurname = users[actualIndex].username,
                        description = users[actualIndex].description,
                        online = users[actualIndex].online,
                        lastSeen = users[actualIndex].lastSeen,
                        navigateToAccountDetail = { onItemClicked(users[actualIndex]) }
                    )
                }
            }
        }
    }
}


@Composable
fun RestaurantsContent(
    users: List<Restaurant>,
    onItemClicked: (Restaurant) -> Unit,
    myId: String,
    myName: String,
    myPhoto: String,
    myStory: Story?,
    stories: List<Story>,
    onCreateStoryClick: () -> Unit,
    onStoryWatchClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ){
        val totalSize = users.size + users.size / 3
        item {
            StoryRoute(
                myId = myId,
                myName = myName,
                myPhoto = myPhoto,
                stories = stories,
                myItem = myStory,
                onCreateStoryClick = onCreateStoryClick,
                onStoryWatchClick = onStoryWatchClick
            )
        }
        items(totalSize) { archive ->
            if ((archive + 1) % 4 == 0) {
                NativeAdSmall(modifier = Modifier.padding(vertical = 8.dp))
            } else {
                val actualIndex = archive - (archive / 4)
                if (actualIndex < users.size) {
                    UserItem(
                        photo = users[actualIndex].photo,
                        nameSurname = users[actualIndex].username,
                        description = users[actualIndex].description,
                        online = users[actualIndex].online,
                        lastSeen = users[actualIndex].lastSeen,
                        navigateToAccountDetail = { onItemClicked(users[actualIndex]) }
                    )
                }
            }
        }
    }
}


@Composable
fun HotelsContent(
    users: List<Hotel>,
    onItemClicked: (Hotel) -> Unit,
    myName: String,
    myPhoto: String,
    stories: List<Story>,
    myId: String,
    myStory: Story?,
    onCreateStoryClick: () -> Unit,
    onStoryWatchClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ){
        val totalSize = users.size + users.size / 3
        item {
            StoryRoute(
                myId = myId,
                myName = myName,
                myPhoto = myPhoto,
                stories = stories,
                myItem = myStory,
                onCreateStoryClick = onCreateStoryClick,
                onStoryWatchClick = onStoryWatchClick
            )
        }
        items(totalSize) { archive ->
            if ((archive + 1) % 4 == 0) {
                NativeAdSmall(modifier = Modifier.padding(vertical = 8.dp))
            } else {
                val actualIndex = archive - (archive / 4)
                if (actualIndex < users.size) {
                    UserItem(
                        photo = users[actualIndex].photo,
                        nameSurname = users[actualIndex].username,
                        description = users[actualIndex].description,
                        online = users[actualIndex].online,
                        lastSeen = users[actualIndex].lastSeen,
                        navigateToAccountDetail = { onItemClicked(users[actualIndex]) }
                    )
                }
            }
        }
    }
}



/** bunu 100 dolar degerınde uyelıkte ver
@Preview
@Composable
private fun CartesianPoints() {
    // [START android_compose_shapes_custom_vertices]
    val vertices = remember {
        val radius = 1f
        val radiusSides = 0.8f
        val innerRadius = .1f
        floatArrayOf(
            radialToCartesian(radiusSides, 0f.toRadians()).x,
            radialToCartesian(radiusSides, 0f.toRadians()).y,
            radialToCartesian(radius, 90f.toRadians()).x,
            radialToCartesian(radius, 90f.toRadians()).y,
            radialToCartesian(radiusSides, 180f.toRadians()).x,
            radialToCartesian(radiusSides, 180f.toRadians()).y,
            radialToCartesian(radius, 250f.toRadians()).x,
            radialToCartesian(radius, 250f.toRadians()).y,
            radialToCartesian(innerRadius, 270f.toRadians()).x,
            radialToCartesian(innerRadius, 270f.toRadians()).y,
            radialToCartesian(radius, 290f.toRadians()).x,
            radialToCartesian(radius, 290f.toRadians()).y,
        )
    }
    // [END android_compose_shapes_custom_vertices]

    // [START android_compose_shapes_custom_vertices_draw]
    val rounding = remember {
        val roundingNormal = 0.6f
        val roundingNone = 0f
        listOf(
            CornerRounding(roundingNormal),
            CornerRounding(roundingNone),
            CornerRounding(roundingNormal),
            CornerRounding(roundingNormal),
            CornerRounding(roundingNone),
            CornerRounding(roundingNormal),
        )
    }

    val polygon = remember(vertices, rounding) {
        RoundedPolygon(
            vertices = vertices,
            perVertexRounding = rounding
        )
    }
    Box(
        modifier = Modifier
            .drawWithCache {
                val roundedPolygonPath = polygon.toPath().asComposePath()
                onDrawBehind {
                    scale(size.width * 0.5f, size.width * 0.5f) {
                        translate(size.width * 0.5f, size.height * 0.5f) {
                            drawPath(roundedPolygonPath, color = Color(0xFFF15087))
                        }
                    }
                }
            }
            .size(400.dp)
    )
    // [END android_compose_shapes_custom_vertices_draw]
}



// [START android_compose_shapes_radial_to_cartesian]
internal fun Float.toRadians() = this * PI.toFloat() / 180f

internal val PointZero = PointF(0f, 0f)
internal fun radialToCartesian(
    radius: Float,
    angleRadians: Float,
    center: PointF = PointZero
) = directionVectorPointF(angleRadians) * radius + center

internal fun directionVectorPointF(angleRadians: Float) =
    PointF(cos(angleRadians), sin(angleRadians))
// [END android_compose_shapes_radial_to_cartesian]
 */
