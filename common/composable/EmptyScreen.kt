package com.kapirti.social_chat_food_video.common.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kapirti.social_chat_food_video.R

@Composable
fun EmptyScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(contentPadding)
            .padding(64.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty_timeline),
            contentDescription = null,
        )
        Text(
            text = stringResource(R.string.timeline_empty_title),
            modifier = Modifier.padding(top = 64.dp),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(R.string.timeline_empty_message),
            textAlign = TextAlign.Center,
        )
    }
}
