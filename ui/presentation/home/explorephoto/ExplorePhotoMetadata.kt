package com.kapirti.social_chat_food_video.ui.presentation.home.explorephoto

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kapirti.social_chat_food_video.R
import com.kapirti.social_chat_food_video.common.composable.NoSurfaceImage
import com.kapirti.social_chat_food_video.common.ext.getRelativeTime
import com.kapirti.social_chat_food_video.model.ExplorePhotoVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ExplorePhotoMetadataOverlay(
    mediaItem: ExplorePhotoVideo,
    onReportClick: () -> Unit,
    onPublisherClick: () -> Unit,
    onFavoriteClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isOpened: Boolean = false,
    isSelected: Boolean = false,
) {
    Box(modifier = modifier.fillMaxSize().zIndex(999f)) {
        Row(modifier = Modifier.align(Alignment.BottomStart)) {
            Card(
                modifier = modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .semantics { selected = isSelected }
                    .clip(CardDefaults.shape)
                    //  .combinedClickable(
                    //    onClick = { navigateToDetail(email.id) },
                    //  onLongClick = { toggleSelection(email.id) }
                    //)
                    .clip(CardDefaults.shape),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else if (isOpened) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        val clickModifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onPublisherClick() }
                        AnimatedContent(targetState = isSelected, label = "avatar") { selected ->
                            if (selected) {
                                SelectedProfileImage(clickModifier)
                            } else {
                                ReplyProfileImage(
                                    mediaItem.userPhoto,
                                    mediaItem.username,
                                    clickModifier
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .clickable { onPublisherClick() },
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = mediaItem.username,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = mediaItem.dateOfCreation?.let { getRelativeTime( it.toDate().time) }.toString() ?: "",
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                        IconButton(
                            onClick = onFavoriteClick,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.heart_thin_icon),
                                modifier = Modifier.size(20.dp),
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                        IconButton(
                            onClick = onReportClick,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                modifier = Modifier.size(20.dp),
                                contentDescription = "Report",
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    Text(
                        text = mediaItem.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun SelectedProfileImage(modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun ReplyProfileImage(
    imageUrl: String,
    description: String,
    modifier: Modifier = Modifier
) {
    NoSurfaceImage(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape),
        imageUrl = imageUrl,
        contentDescription = description,
    )
}
