package com.kapirti.social_chat_food_video.common.composable
/**
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.kapirti.social_chat_food_video.R.string as AppText
import com.kapirti.social_chat_food_video.common.EmptyContent
import com.kapirti.social_chat_food_video.common.ext.timeline.InputBarTimeline
import com.kapirti.social_chat_food_video.ui.theme.crane_purple_800
import com.kapirti.social_chat_food_video.ui.theme.crane_purple_900
import com.kapirti.social_chat_food_video.ui.theme.crane_red
import com.kapirti.social_chat_food_video.ui.theme.crane_white


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartialBottomSheet(
    comments: List<String>,
    onSendClick: (String) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    IconButton(
        onClick = {
            showBottomSheet = true
        }
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Comment,
            contentDescription = stringResource(AppText.cd_icon),
            tint = crane_purple_900
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false },
            containerColor = crane_purple_800,
        ) {
            BottomSheetContent(comments = comments, onSendClick)
        }
    }
}

@Composable
private fun BottomSheetContent(comments: List<String>, onSendClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .padding(bottom = 4.dp)
        ) {
            item {
                InputBarTimeline(
                    //  input = input,
                    //  onInputChanged = onInputChanged,
                    // onSendClick = onSendClick,
                    // onCameraClick = onCameraClick,
                    // onPhotoPickerClick = onPhotoPickerClick,
                    // contentPadding = PaddingValues(0.dp),
                    // sendEnabled = sendEnabled,
                    onMessageSent = onSendClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.ime.exclude(WindowInsets.navigationBars)),
                )
            }

            if (comments.isEmpty()) {
                item {
                    EmptyContent(icon = Icons.AutoMirrored.Filled.Comment, label = stringResource(AppText.no_comments))
                }
            }

            items(comments) { index ->
                // val sensorValue: Map.Entry<String, Any> = sensorValueList[index]
                CommentItem(
                    text = index
                )
            }
        }
    }
}

@Composable
private fun CommentItem(text: String) {
    Column() {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = crane_white,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
        )
        HorizontalDivider(color = crane_red, modifier = Modifier.padding(horizontal = 4.dp))
    }
}
*/
