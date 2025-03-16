package com.kapirti.social_chat_food_video.ui.presentation.calls

import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallMissed
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kapirti.social_chat_food_video.R
import kotlin.time.Duration

enum class CallType {
    INCOMING,
    OUTGOING,
    MISSED
}

@Composable
fun CallType.Icon() {
    val icon = when(this) {
        CallType.INCOMING -> Icons.AutoMirrored.Filled.CallReceived
        CallType.OUTGOING -> Icons.AutoMirrored.Filled.CallMade
        CallType.MISSED -> Icons.AutoMirrored.Filled.CallMissed
    }

    Icon(
        imageVector = icon,
        contentDescription = stringResource(id = R.string.cd_menu),
        //contentDescription = stringResource(id = R.string.cd_menu),
        tint = MaterialTheme.colorScheme.primary
    )

}

//fun humanReadableDuration(s: String): String = Duration.parse(s).toString()
//    .substring(2).toLowerCase().replace(Regex("[hms](?!\$)")) { "${it.value} " }

//fun mockCall(): RegisteredCall {
//    return RegisteredCall(
//        "the caller",
//        CallType.entries[abs(id) % CallType.entries.size],
//        Random(5000).nextInt(0..1000).seconds
//    )
//}
//
//fun dummyCalls(size: Int): List<RegisteredCall> {
//    return (0 until size).map { mockCall() }
//}
