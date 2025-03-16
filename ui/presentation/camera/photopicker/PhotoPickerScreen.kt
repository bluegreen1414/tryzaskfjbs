package com.kapirti.social_chat_food_video.ui.presentation.camera.photopicker

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kapirti.social_chat_food_video.R
import com.kapirti.social_chat_food_video.common.composable.BackDoneToolbar
import com.kapirti.social_chat_food_video.common.ext.fieldModifier
import com.kapirti.social_chat_food_video.ui.presentation.edit.question.PhotoDefaultImage
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.kapirti.social_chat_food_video.common.composable.KapirtiIcons

@Composable
fun PhotoPickerScreen(
    cameraType: String,
    isError: Boolean,
    isDoneBtnWorking: Boolean,
    value: String,
    save: () -> Unit,
    popUp: () -> Unit,
    imageUri: Uri?,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasPhoto = imageUri != null
    val iconResource = if (hasPhoto) {
        Icons.Filled.SwapHoriz
    } else {
        Icons.Filled.AddAPhoto
    }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BackDoneToolbar(
                isDoneBtnWorking = isDoneBtnWorking,
                popUp = popUp,
                title = cameraType,
            ) { save() }
        }
    ) { innerPadding ->
        Column(modifier.padding(innerPadding).fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally) {

            OutlinedButton(
                onClick = { onClick() },
                shape = MaterialTheme.shapes.small,
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxWidth(0.7f).height(300.dp)
            ) {
                Column {
                    if (hasPhoto) {

                        val mimeType = context.contentResolver.getType(imageUri ?: return@Column)
                        if (mimeType != null) {
                            when {
                                mimeType.startsWith("image/") -> {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(imageUri)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(96.dp)
                                            .aspectRatio(4 / 3f)
                                    )
                                }

                                mimeType.startsWith("video/") -> {
                                    Icon(KapirtiIcons.Done, null, Modifier.size(200.dp))
                                }
                            }
                        }
                    } else {
                        PhotoDefaultImage(
                            modifier = Modifier.padding(
                                horizontal = 86.dp,
                                vertical = 74.dp
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.BottomCenter)
                            .padding(vertical = 26.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = iconResource,
                            contentDescription = stringResource(AppText.add_photo)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(
                                id = if (hasPhoto) {
                                    AppText.retake_photo
                                } else {
                                    AppText.add_photo
                                }
                            )
                        )
                    }
                }
            }
            if (cameraType != "chat") {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = { Text(stringResource(R.string.description)) },
                    modifier = Modifier
                        .fieldModifier()
                        .height(300.dp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Blue,
                        unfocusedLabelColor = Color.Blue,//Transparent,
                        cursorColor = MaterialTheme.colorScheme.secondary
                    ),
                    isError = isError
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
