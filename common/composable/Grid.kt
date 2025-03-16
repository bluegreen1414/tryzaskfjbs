package com.kapirti.social_chat_food_video.common.composable

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import coil.compose.rememberAsyncImagePainter
import com.kapirti.social_chat_food_video.common.ext.post
import com.kapirti.social_chat_food_video.model.ExploreType
import com.kapirti.social_chat_food_video.model.ExploreUserItem
import kotlin.math.ceil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun StaggeredVerticalGrid(
    modifier: Modifier = Modifier,
    maxColumnWidth: Dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        check(constraints.hasBoundedWidth) {
            "Unbounded width not supported"
        }
        val columns = ceil(constraints.maxWidth / maxColumnWidth.toPx()).toInt()
        val columnWidth = constraints.maxWidth / columns
        val itemConstraints = constraints.copy(maxWidth = columnWidth)
        val colHeights = IntArray(columns) { 0 } // track each column's height
        val placeables = measurables.map { measurable ->
            val column = shortestColumn(colHeights)
            val placeable = measurable.measure(itemConstraints)
            colHeights[column] += placeable.height
            placeable
        }

        val height = colHeights.maxOrNull()?.coerceIn(constraints.minHeight, constraints.maxHeight)
            ?: constraints.minHeight
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            val colY = IntArray(columns) { 0 }
            placeables.forEach { placeable ->
                val column = shortestColumn(colY)
                placeable.place(
                    x = columnWidth * column,
                    y = colY[column]
                )
                colY[column] += placeable.height
            }
        }
    }
}

private fun shortestColumn(colHeights: IntArray): Int {
    var minHeight = Int.MAX_VALUE
    var column = 0
    colHeights.forEachIndexed { index, height ->
        if (height < minHeight) {
            minHeight = height
            column = index
        }
    }
    return column
}


@Composable
fun PostGrid(postsList: List<ExploreUserItem>, onItemClick: () -> Unit) {
    if (postsList.isEmpty()) return
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(4.dp)
    ) {
        items(postsList) { postItem ->
            val mimeType = postItem.type
            if (mimeType.contains(ExploreType.PHOTO.toString())) {
                Image(
                    painter = rememberAsyncImagePainter(postItem.uri),
                    contentDescription = "Post Image",
                    modifier = Modifier.post().clickable { onItemClick() },
                    contentScale = ContentScale.Crop
                )
            } else if (mimeType.contains(ExploreType.VIDEO.toString())) {
                VideoPreview(
                    videoUri = postItem.uri,
                    onClick = { onItemClick()},
                )
            }
        }
    }
}

@Composable
private fun VideoPreview(videoUri: String, onClick: () -> Unit) {
    val bitmapState = produceState<Bitmap?>(initialValue = null) {
        withContext(Dispatchers.IO) {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(videoUri, HashMap<String, String>())
            value = mediaMetadataRetriever.frameAtTime
        }
    }

    bitmapState.value?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "post image",
            modifier = Modifier.post().clickable { onClick() },
            contentScale = ContentScale.Crop
        )
    }
}
