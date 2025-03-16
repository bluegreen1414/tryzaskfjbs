package com.kapirti.social_chat_food_video.ui.presentation.home.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.kapirti.social_chat_food_video.common.complex.ApplyPolygonAsClipImage
import com.kapirti.social_chat_food_video.common.ext.getRelativeTime
import com.kapirti.social_chat_food_video.ui.theme.GREEN450

@Composable
fun UserItem(
    photo: String,
    nameSurname: String,
    description: String,
    online: Boolean,
    lastSeen: Timestamp?,
    navigateToAccountDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clip(CardDefaults.shape)
            .clickable { navigateToAccountDetail() }
            .clip(CardDefaults.shape),
        colors = CardDefaults.cardColors(
            containerColor =
            if (online) GREEN450
            else MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            ApplyPolygonAsClipImage(photo = photo)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = nameSurname,
                    style = MaterialTheme.typography.labelMedium
                )
                if (!online) {
                    Text(
                        text = lastSeen?.let { "Last seen :${getRelativeTime(lastSeen.toDate().time)}" }
                            ?: "Last seen :",
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
