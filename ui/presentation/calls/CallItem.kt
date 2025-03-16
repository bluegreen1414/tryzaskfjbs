package com.kapirti.social_chat_food_video.ui.presentation.calls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import com.kapirti.social_chat_food_video.model.CallRecord
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun CallItem(
    registeredCall: CallRecord,
    onClick: () -> Unit = {},
) {
    val  duration = (registeredCall.callEnd!!.seconds - registeredCall.callStart!!.seconds).toDuration(
        DurationUnit.SECONDS)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            registeredCall.callType?.Icon()
        }
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = registeredCall.peerName,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = duration.toString(),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
