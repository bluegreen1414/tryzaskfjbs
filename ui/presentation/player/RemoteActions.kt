package com.kapirti.social_chat_food_video.ui.presentation.player

import android.app.PendingIntent
import android.app.RemoteAction
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.kapirti.social_chat_food_video.R

@RequiresApi(Build.VERSION_CODES.O)
fun listOfRemoteActions(isPlaying: Boolean, context: Context): List<RemoteAction> {
    return listOf(
        buildRemoteAction(
            R.drawable.ic_rw_24dp,
            R.string.rw_title,
            REQUEST_RW,
            EXTRA_CONTROL_RW,
            context = context,
        ),
        if (isPlaying) {
            buildRemoteAction(
                R.drawable.ic_pause_24dp,
                R.string.pause_title,
                REQUEST_PAUSE,
                EXTRA_CONTROL_PAUSE,
                context = context,
            )
        } else {
            buildRemoteAction(
                R.drawable.ic_play_24dp,
                R.string.play_title,
                REQUEST_PLAY,
                EXTRA_CONTROL_PLAY,
                context = context,
            )
        },
        buildRemoteAction(
            R.drawable.ic_ff_24dp,
            R.string.ff_title,
            REQUEST_FF,
            EXTRA_CONTROL_FF,
            context = context,
        ),
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun buildRemoteAction(
    @DrawableRes iconResId: Int,
    @StringRes titleResId: Int,
    requestCode: Int,
    controlType: Int,
    context: Context,
): RemoteAction {
    return RemoteAction(
        Icon.createWithResource(context, iconResId),
        context.getString(titleResId),
        context.getString(titleResId),
        PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(ACTION_BROADCAST_CONTROL)
                .setPackage(context.packageName)
                .putExtra(EXTRA_CONTROL_TYPE, controlType),
            PendingIntent.FLAG_IMMUTABLE,
        ),
    )
}
