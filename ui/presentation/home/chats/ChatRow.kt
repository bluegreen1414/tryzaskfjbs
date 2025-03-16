package com.kapirti.social_chat_food_video.ui.presentation.home.chats

import com.kapirti.social_chat_food_video.common.composable.NoSurfaceImage
import com.kapirti.social_chat_food_video.common.ext.toReadableString
import com.kapirti.social_chat_food_video.model.ChatRoomFromUserChat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kapirti.social_chat_food_video.common.ext.getRelativeTime

@Composable
fun ChatRow(
    meId: String,
    chat: ChatRoomFromUserChat,
    onClick: ((String) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val contact = chat.partnerUsername

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(onClick = {onClick(contact)}) else Modifier,
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        NoSurfaceImage(
            imageUrl = chat.partnerPhoto,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = chat.partnerUsername,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
            )
            Text(
                text = if(chat.lastMessageSenderId == meId){
                    "You: ${chat.lastMessage}"
                } else chat.lastMessage,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp),
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
            )
        }
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            chat.unread?.let {
                if (it != 0){
                    Text(
                        text = it.toString(),
                        fontSize = 10.sp,
                        maxLines = 1,
                        color = Color.White,
                        modifier = Modifier.background(
                            color = Color.Red,
                            shape = CircleShape,
                        )
                    )
                }
            }


            Text(
                text = chat.lastMessageTime?.let { getRelativeTime(it.toDate().time).toString()} ?: "",
                fontSize = 10.sp,
                maxLines = 1,
            )
        }
    }
}
