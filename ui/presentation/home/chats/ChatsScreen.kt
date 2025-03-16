package com.kapirti.social_chat_food_video.ui.presentation.home.chats

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kapirti.social_chat_food_video.model.ChatRoomFromUserChat

@Composable
fun ChatsScreen(
    meId: String,
    chats: List<ChatRoomFromUserChat>,
    onChatClicked: (chatId: String) -> Unit,
    contentPadding: PaddingValues,
    //onChatClicked: (firstId: String, secondId: String, name:String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(contentPadding = contentPadding,) {
        items(items = chats) { chat ->
            ChatRow(
                meId = meId,
                chat = chat,
                //onClick = { onChatClicked(chat.chatWithLastMessage.id) },
                //onClick = { name -> onChatClicked(chat.roomId) },
                onClick = { name -> onChatClicked(chat.chatId)}
            )
        }
    }
}
