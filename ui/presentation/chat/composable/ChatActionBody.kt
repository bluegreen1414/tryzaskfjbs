package com.kapirti.social_chat_food_video.ui.presentation.chat.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import com.kapirti.social_chat_food_video.R.string as AppText

@Composable
fun ChatActionBody(
    onReportClick: () -> Unit,
    onBlockClick: () -> Unit
){
    Column(modifier = Modifier.fillMaxWidth(0.7f)) {
        ActionButton(AppText.block, onBlockClick)
        ActionButton(AppText.report, onReportClick)
    }
}

@Composable
private fun ActionButton(
    @StringRes text: Int,
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            stringResource(text),
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic
        )
    }
}

