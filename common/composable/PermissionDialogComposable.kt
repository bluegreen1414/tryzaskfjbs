package com.kapirti.social_chat_food_video.common.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun NotificationPermissionCard(
    shouldShowRationale: Boolean,
    onGrantClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
    ) {
        Text(
            text = "To be notified when you have new messages, please grant the notification permission.",
            modifier = Modifier.padding(16.dp),
        )
        if (shouldShowRationale) {
            Text(
                text = "Please grant the notification permission so that the app can demonstrate usage of the Notification APIs.",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopEnd,
        ) {
            Button(onClick = onGrantClick) {
                Text(text = "GRANT")
            }
        }
    }
}





/**
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.kapirti.social_chat_food_video.common.ext.alertDialog
import com.kapirti.social_chat_food_video.common.ext.textButton
import com.kapirti.social_chat_food_video.ui.theme.BrightOrange

@Composable
fun PermissionDialog(onRequestPermission: () -> Unit) {
var showWarningDialog by remember { mutableStateOf(true) }

if (showWarningDialog) {
AlertDialog(
modifier = Modifier.alertDialog(),
title = { Text(stringResource(id = AppText.notification_permission_title)) },
text = { Text(stringResource(id = AppText.notification_permission_description)) },
confirmButton = {
TextButton(
onClick = {
onRequestPermission()
showWarningDialog = false
},
modifier = Modifier.textButton(),
colors = ButtonDefaults.buttonColors(
containerColor = BrightOrange,
contentColor = Color.White
),
) { Text(text = stringResource(AppText.request_notification_permission)) }
},
onDismissRequest = { },
)
}
}

@Composable
fun RationaleDialog() {
var showWarningDialog by remember { mutableStateOf(true) }

if (showWarningDialog) {
AlertDialog(
modifier = Modifier.alertDialog(),
title = { Text(stringResource(id = AppText.notification_permission_title)) },
text = { Text(stringResource(id = AppText.notification_permission_settings)) },
confirmButton = {
TextButton(
onClick = { showWarningDialog = false },
modifier = Modifier.textButton(),
colors = ButtonDefaults.buttonColors(
containerColor = BrightOrange,
contentColor = Color.White
)
) { Text(text = stringResource(AppText.ok)) }
},
onDismissRequest = { showWarningDialog = false }
)
}
}
 */
