/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kapirti.social_chat_food_video.common.ext

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.ui.graphics.vector.ImageVector
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.compose.material.icons.automirrored.filled.ChromeReaderMode
import com.kapirti.social_chat_food_video.core.constants.ConsGender.FEMALE
import com.kapirti.social_chat_food_video.core.constants.ConsGender.MALE
import com.kapirti.social_chat_food_video.core.constants.Hobby.DANCE
import com.kapirti.social_chat_food_video.core.constants.Hobby.DRAW
import com.kapirti.social_chat_food_video.core.constants.Hobby.PLAY_GAMES
import com.kapirti.social_chat_food_video.core.constants.Hobby.READ
import com.kapirti.social_chat_food_video.core.constants.Hobby.WATCH_MOVIES
import com.kapirti.social_chat_food_video.core.constants.Hobby.WORK_OUT

fun hobbyIcon(text: String): ImageVector {
    return when(text){
        READ -> Icons.AutoMirrored.Filled.ChromeReaderMode
        WORK_OUT -> Icons.Default.SportsMartialArts
        DRAW -> Icons.Default.Draw
        PLAY_GAMES -> Icons.Default.Games
        DANCE -> Icons.Default.Nightlife
        WATCH_MOVIES -> Icons.Default.PlayCircle

        else -> Icons.Default.VideogameAsset
    }
}

fun genderIcon(cons: String): ImageVector{
    return when(cons){
        FEMALE -> Icons.Default.Female
        MALE -> Icons.Default.Male
        else -> Icons.Default.Female
    }
}

fun genderText(cons: String): Int{
    return when(cons){
        FEMALE -> AppText.female
        MALE -> AppText.male
        else -> AppText.female
    }
}
