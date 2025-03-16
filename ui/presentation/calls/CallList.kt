package com.kapirti.social_chat_food_video.ui.presentation.calls

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import com.kapirti.social_chat_food_video.model.CallRecord

@Composable
fun CallList(
    recordings: List<CallRecord>,
    innerPadding: PaddingValues,
    onClick: (index: Int, recording: CallRecord) -> Unit,
) {
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState, contentPadding = innerPadding) {
        itemsIndexed(recordings) { index, recording ->
            CallItem(recording) { onClick(index, recording) }
        }
    }
}
