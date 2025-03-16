package com.kapirti.social_chat_food_video.ui.presentation.camera.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kapirti.social_chat_food_video.R
import com.kapirti.social_chat_food_video.common.composable.KapirtiIcons
import com.kapirti.social_chat_food_video.ui.presentation.edit.question.PhotoDefaultImage

@Composable
fun AddScreen(
    onVideoClick: () -> Unit,
    onPhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    OutlinedButton(
        onClick = {},
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(),
        modifier = modifier
    ) {
        Column {
            PhotoDefaultImage(
                modifier = Modifier.padding(
                    horizontal = 86.dp,
                    vertical = 74.dp
                )
            )

            Column(
                modifier = Modifier.fillMaxWidth()
                    .wrapContentSize(Alignment.BottomCenter)
                    .padding(vertical = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.BottomCenter)
                        .padding(vertical = 26.dp).clickable { onPhotoClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = KapirtiIcons.Camera,
                        contentDescription = stringResource(R.string.add_photo)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.add_photo)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.BottomCenter)
                        .padding(vertical = 26.dp).clickable { onVideoClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = KapirtiIcons.VideoLibrary,
                        contentDescription = stringResource(R.string.add_video)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.add_video)
                    )
                }
            }
        }
    }
}
