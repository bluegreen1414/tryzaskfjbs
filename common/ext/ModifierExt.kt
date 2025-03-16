package com.kapirti.social_chat_food_video.common.ext

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

fun Modifier.textButton(): Modifier {
    return this.fillMaxWidth().padding(16.dp, 8.dp, 16.dp, 0.dp)
}

fun Modifier.basicButton(): Modifier {
    return this.fillMaxWidth().padding(16.dp, 8.dp)
}

fun Modifier.card(): Modifier {
    return this.padding(16.dp, 0.dp, 16.dp, 8.dp)
}

fun Modifier.contextMenu(): Modifier {
    return this.wrapContentWidth()
}

fun Modifier.post(): Modifier {
    return this.aspectRatio(9 / 16f)
        .clip(RectangleShape)
}

fun Modifier.fieldModifier(): Modifier {
    return this.fillMaxWidth().padding(16.dp, 4.dp)
}

fun Modifier.toolbarActions(): Modifier {
    return this.wrapContentSize(Alignment.TopEnd)
}

fun Modifier.spacer(): Modifier {
    return this.fillMaxWidth().padding(12.dp)
}

fun Modifier.smallSpacer(): Modifier {
    return this.fillMaxWidth().height(8.dp)
}

fun Modifier.supportWideScreen() = this
    .fillMaxWidth()
    .wrapContentWidth(align = Alignment.CenterHorizontally)
    .widthIn(max = 840.dp)

val tabContainerModifier = Modifier
    .fillMaxWidth()
    .wrapContentWidth(Alignment.CenterHorizontally)
