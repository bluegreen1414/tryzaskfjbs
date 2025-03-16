package com.kapirti.social_chat_food_video.ui.presentation.chat

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kapirti.social_chat_food_video.R
import com.kapirti.social_chat_food_video.common.composable.DialogCancelButton
import com.kapirti.social_chat_food_video.common.composable.DialogConfirmButton
import com.kapirti.social_chat_food_video.core.viewmodel.IncludeAccountViewModel
import com.kapirti.social_chat_food_video.model.UserFlirt
import com.kapirti.social_chat_food_video.ui.presentation.chat.composable.ChatActionBody

@Composable
fun ChatRoute(
    chatId: String,
    //firstUserId: String,
    //secondUserId: String,
    //name: String,
    popUp: () -> Unit,
    foreground: Boolean = false,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onCameraClick: () -> Unit,
    onVideoClick: (uri: String) -> Unit = {},
    onVoiceCallClicked: () -> Unit = {},
    includeAccountViewModel: IncludeAccountViewModel,
    onUserClick: (String) -> Unit,
    uriText: String? = null,
    uriMimeType: String? = null,
    viewModel: ChatViewModel = hiltViewModel(),
) {

    LaunchedEffect(chatId) {
        viewModel.setChatId(chatId)
        if (uriText != null && uriMimeType != null) {
            //viewModel.prefillInput(prefilledText)
//            viewModel.sendMediaMessage(uriText, uriMimeType)
        }
    }

    val uiState by viewModel.uiState

    val input by viewModel.input.collectAsStateWithLifecycle()

    uiState.chatRoom?.let { chatRoom ->
        ChatScreen(
            //chat = c,
            username = viewModel.partnerUsername ?: "",
            photo = viewModel.partnerPhoto ?: "",
            chatState = "uiState.otherUserChatState",
            typing = viewModel.isTyping ?: false,
            online = viewModel.online ?: false,
            lastSeen = viewModel.lastSeen,
            messages = uiState.messages,
            input = input,
            sendEnabled = true,
            onBackPressed = onBackPressed,
            onInputChanged = { viewModel.updateInput(it, chatId) },
            onSendClick = { content -> viewModel.send(content) },
            onCameraClick = { viewModel.onCameraClick(onCameraClick) },
            onVoiceCallClicked = {
                android.util.Log.d("myTag", "voice call should be triggered")
                viewModel.callFriend()
                onVoiceCallClicked.invoke()
            },
            onVideoClick = onVideoClick,
            onActionClick = { viewModel.onActionClick() },
            onTopBarClick = { onUserClick(viewModel.partnerId ?: "") } ,
            modifier = modifier.clip(RoundedCornerShape(5)),
            )
    }
//    LifecycleEffect(
//        onResume = { viewModel.setForeground(foreground) },
//        onPause = { viewModel.setForeground(false) },
//    )


    if (viewModel.showActionDialog.value) {
        AlertDialog(
            title = {
                ChatActionBody(
                    onBlockClick = viewModel::onBlockClick,
                    onReportClick = viewModel::onReportClick
                )
            },
            confirmButton = {
                DialogConfirmButton(R.string.ok) {
                    viewModel.showActionDialog.value = false
                }
            },
            onDismissRequest = { viewModel.showActionDialog.value = false }
        )
    }
    if (viewModel.showBlockDialog.value) {
        AlertDialog(
            title = { Text(stringResource(R.string.block)) },
            text = { Text(stringResource(R.string.block_user)) },
            dismissButton = {
                DialogCancelButton(R.string.cancel) {
                    viewModel.showBlockDialog.value = false
                }
            },
            confirmButton = {
                DialogConfirmButton(R.string.block) {
                    viewModel.onBlockButtonClick(popUp)
                    viewModel.showBlockDialog.value = false
                }
            },
            onDismissRequest = { viewModel.showBlockDialog.value = false }
        )
    }
    if (viewModel.showReportDialog.value) {
        AlertDialog(
            title = { Text(stringResource(R.string.report)) },
            text = { Text(stringResource(R.string.report_user)) },
            dismissButton = {
                DialogCancelButton(R.string.cancel) {
                    viewModel.showReportDialog.value = false
                }
            },
            confirmButton = {
                DialogConfirmButton(R.string.report) {
                    viewModel.onReportButtonClick(popUp)
                    viewModel.showReportDialog.value = false
                }
            },
            onDismissRequest = { viewModel.showReportDialog.value = false }
        )
    }
}
