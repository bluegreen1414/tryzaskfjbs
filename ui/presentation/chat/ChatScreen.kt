package com.kapirti.social_chat_food_video.ui.presentation.chat

import android.content.ClipDescription
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.Timestamp
import com.kapirti.social_chat_food_video.model.GetChatMessage
import com.kapirti.social_chat_food_video.ui.presentation.chat.composable.ChannelNameBar
import com.kapirti.social_chat_food_video.ui.presentation.chat.composable.MessageList
import com.kapirti.social_chat_food_video.ui.presentation.chat.composable.UserInput
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(
    //chat: ChatDetail,
    username: String,
    photo: String,
    typing: Boolean,
    online: Boolean,
    lastSeen: Timestamp?,
    chatState: String,
    messages: List<GetChatMessage>,
    input: String,
    sendEnabled: Boolean,
    onBackPressed: () -> Unit = {},
    onInputChanged: (String) -> Unit,
    onSendClick: (String) -> Unit,
    onCameraClick: () -> Unit,
    onVideoClick: (uri: String) -> Unit,
    onVoiceCallClicked: () -> Unit,
    onActionClick: () -> Unit,
    onTopBarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var background by remember {
        mutableStateOf(Color.Transparent)
    }

    var borderStroke by remember {
        mutableStateOf(Color.Transparent)
    }
    val dragAndDropCallback = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val clipData = event.toAndroidDragEvent().clipData

                if (clipData.itemCount < 1) {
                    return false
                }
                /**
                uiState.addMessage(
                Message(authorMe, clipData.getItemAt(0).text.toString(), timeNow)
                )*/

                return true
            }

            override fun onStarted(event: DragAndDropEvent) {
                super.onStarted(event)
                borderStroke = Color.Red
            }

            override fun onEntered(event: DragAndDropEvent) {
                super.onEntered(event)
                background = Color.Red.copy(alpha = .3f)
            }

            override fun onExited(event: DragAndDropEvent) {
                super.onExited(event)
                background = Color.Transparent
            }

            override fun onEnded(event: DragAndDropEvent) {
                super.onEnded(event)
                background = Color.Transparent
                borderStroke = Color.Transparent
            }
        }
    }


    val context = LocalContext.current
    Scaffold(
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ChannelNameBar(
                username = username,
                photo = photo,
                typing = typing,
                online = online,
                lastSeen = lastSeen,
                onActionClick = onActionClick,
                onBackPressed = onBackPressed,
                scrollBehavior = scrollBehavior,
                onVoiceCallClicked = { Toast.makeText(context, "This function is not working now", Toast.LENGTH_SHORT).show()},
             //   onVoiceCallClicked = onVoiceCallClicked,
                onTopBarClick = onTopBarClick
            )
        },
    ) { innerPadding ->
        val layoutDirection = LocalLayoutDirection.current
        Column(
            Modifier.fillMaxSize().padding(innerPadding)
                .background(color = background)
                .border(width = 2.dp, color = borderStroke)
                .dragAndDropTarget(shouldStartDragAndDrop = { event ->
                    event
                        .mimeTypes()
                        .contains(
                            ClipDescription.MIMETYPE_TEXT_PLAIN
                        )
                }, target = dragAndDropCallback)
        ) {
            MessageList(
                messages = messages,
                contentPadding = innerPadding.copy(layoutDirection, bottom = 16.dp),
                modifier = Modifier.fillMaxWidth().weight(1f),
                scrollState = scrollState,
                onVideoClick = onVideoClick,
            )
            UserInput(
                onMessageSent = { content ->
                    onSendClick(content)
/**                    uiState.addMessage(
                        Message(authorMe, content, timeNow)
                    )*/
                },
                resetScroll = {
                    scope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                modifier = Modifier.navigationBarsPadding().imePadding(),
                onPicktureClick = onCameraClick,
               // onCameraClick = onCameraClick,
            )
        }

      /**
            InputBar(
                input = input,
                onInputChanged = onInputChanged,
                onSendClick = onSendClick,

                contentPadding = innerPadding.copy(layoutDirection, top = 0.dp),
                sendEnabled = sendEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.ime.exclude(WindowInsets.navigationBars)),
            )
        }*/
    }
}

/**
) { innerPadding ->
    Column {
        val layoutDirection = LocalLayoutDirection.current
        MessageList(
            messages = messages,
            contentPadding = innerPadding.copy(layoutDirection, bottom = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onVideoClick = onVideoClick,
        )
        InputBar(
            input = input,
            onInputChanged = onInputChanged,
            onSendClick = onSendClick,
            onCameraClick = onCameraClick,
            onPhotoPickerClick = onPhotoPickerClick,
            contentPadding = innerPadding.copy(layoutDirection, top = 0.dp),
            sendEnabled = sendEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.ime.exclude(WindowInsets.navigationBars)),
        )
    }
    */



private fun PaddingValues.copy(
    layoutDirection: LayoutDirection,
    start: Dp? = null,
    top: Dp? = null,
    end: Dp? = null,
    bottom: Dp? = null,
) = PaddingValues(
    start = start ?: calculateStartPadding(layoutDirection),
    top = top ?: calculateTopPadding(),
    end = end ?: calculateEndPadding(layoutDirection),
    bottom = bottom ?: calculateBottomPadding(),
)


@Composable
private fun LifecycleEffect(
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val listener = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                onPause()
            }
        }
        lifecycle.addObserver(listener)
        onDispose {
            lifecycle.removeObserver(listener)
        }
    }
}
