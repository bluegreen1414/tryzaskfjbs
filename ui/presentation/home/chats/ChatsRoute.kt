package com.kapirti.social_chat_food_video.ui.presentation.home.chats

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kapirti.social_chat_food_video.common.composable.HomeBackground
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kapirti.social_chat_food_video.common.composable.ChatsAppBar
import com.kapirti.social_chat_food_video.common.composable.EmptyScreen

@Composable
fun ChatsRoute(
    onChatClicked: (String) -> Unit,
    onCallClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatsViewModel = hiltViewModel(),
) {
    val chats by viewModel.chats.collectAsStateWithLifecycle()
    val meId = viewModel.meId

//    val uiState by viewModel.uiState

    Scaffold(
        modifier = modifier,
        topBar = {
            ChatsAppBar(onCallsClick = onCallClick, onProfileClick = onProfileClick)
        },
    ) { contentPadding ->
        HomeBackground(modifier = Modifier.fillMaxSize())
        if(chats.isEmpty()){
            EmptyScreen(contentPadding = contentPadding)
        } else {
            ChatsScreen(
                meId,
                chats,
//            uiState.conversationList,
                onChatClicked = onChatClicked,
                contentPadding = contentPadding,
            )
        }
    }
}
