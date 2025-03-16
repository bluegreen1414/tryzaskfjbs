package com.kapirti.social_chat_food_video.common.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kapirti.social_chat_food_video.R.drawable as AppIcon
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import com.kapirti.social_chat_food_video.R
import kotlin.math.max
import kotlin.math.min
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntSize
import coil.compose.rememberAsyncImagePainter

@Composable
fun SurfaceImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    LuccaSurface(
        color = Color.LightGray,
        elevation = elevation,
        shape = CircleShape,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            placeholder = painterResource(AppIcon.ic_send),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun NoSurfaceImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    colorFilter: ColorFilter? = null
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        placeholder = painterResource(AppIcon.logo),
        error = painterResource(id = AppIcon.logo),
        modifier = modifier,
        contentScale = ContentScale.Crop,
        colorFilter = colorFilter
    )
}


private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun UserImage(
    imageUrl: String,
    onImageClicked: () -> Unit,
    scrollProvider: () -> Int
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }

    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = HzPadding.then(Modifier.statusBarsPadding())
    ) {
        SurfaceImage(
            imageUrl = imageUrl,
            contentDescription = stringResource(id = R.string.cd_profile_photo),
            modifier = Modifier.fillMaxSize().clickable { onImageClicked()}
        )
    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFractionProvider: () -> Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val collapseFraction = collapseFractionProvider()

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2,
            constraints.maxWidth - imageWidth,
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}


@Composable
fun FullScreenPhoto(
    photo: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Scrim(onDismiss, Modifier.fillMaxSize())
        PhotoImage(photo)
    }
}

@Composable
private fun Scrim(onClose: () -> Unit, modifier: Modifier = Modifier) {
    val strClose = stringResource(id = R.string.app_name)
    Box(
        modifier
            .fillMaxSize()
            .pointerInput(onClose) { detectTapGestures { onClose() } }
            .semantics {
                onClick(strClose) { onClose(); true }
            }
            .focusable()
            .onKeyEvent {
                if (it.key == Key.Escape) {
                    onClose(); true
                } else {
                    false
                }
            }
            .background(Color.DarkGray.copy(alpha = 0.75f))
    )
}

@Composable
fun PhotoImage(photo: String, modifier: Modifier = Modifier) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var zoom by remember { mutableStateOf(1f) }

    Image(
        painter = rememberAsyncImagePainter(model = photo),
        contentDescription = photo,
        modifier
            .clipToBounds()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { tapOffset ->
                        zoom = if (zoom > 1f) 1f else 2f
                        offset = calculateDoubleTapOffset(zoom, size, tapOffset)
                    }
                )
            }
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { centroid, pan, gestureZoom, _ ->
                        offset = offset.calculateNewOffset(
                            centroid, pan, zoom, gestureZoom, size
                        )
                        zoom = maxOf(1f, zoom * gestureZoom)
                    }
                )
            }
            .graphicsLayer {
                translationX = -offset.x * zoom
                translationY = -offset.y * zoom
                scaleX = zoom; scaleY = zoom
                transformOrigin = TransformOrigin(0f, 0f)
            }
            .aspectRatio(1f)
    )
}

fun Offset.calculateNewOffset(
    centroid: Offset,
    pan: Offset,
    zoom: Float,
    gestureZoom: Float,
    size: IntSize
): Offset {
    val newScale = maxOf(1f, zoom * gestureZoom)
    val newOffset = (this + centroid / zoom) -
        (centroid / newScale + pan / zoom)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}


fun calculateDoubleTapOffset(
    zoom: Float,
    size: IntSize,
    tapOffset: Offset
): Offset {
    val newOffset = Offset(tapOffset.x, tapOffset.y)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}


@Composable
fun ProfileImage(profileImageUrl: String, online: Boolean = false, navigateProfilePhoto: () -> Unit = {}) {
    val color = if (online) Color.Green else Color.Gray
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .border(2.dp, color, CircleShape)
            .background(Color.Gray)
            .clickable { navigateProfilePhoto() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = rememberAsyncImagePainter(profileImageUrl),
            contentDescription = "Profile Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}
