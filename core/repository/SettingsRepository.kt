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

package com.kapirti.social_chat_food_video.core.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kapirti.social_chat_food_video.R.string as AppText
import com.kapirti.social_chat_food_video.core.constants.Cons.PRIVACY_POLICY_CODE
import com.kapirti.social_chat_food_video.core.constants.Cons.SHARE_CODE

class SettingsRepository(private val context: Context) {

    fun share() {
        val intent = Intent()
        intent.action= Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, SHARE_CODE)
        intent.type="text/plain"
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(Intent.createChooser(intent, context.getString(AppText.app_name)))
    }

    fun rate() {
        val shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(SHARE_CODE))
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    fun privacyPolicy() {
        val shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_CODE))
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}
