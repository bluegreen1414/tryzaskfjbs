package com.kapirti.social_chat_food_video.ui.presentation.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.content.LocusIdCompat
import androidx.core.content.getSystemService
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import com.google.firebase.Timestamp
import com.kapirti.social_chat_food_video.MainActivity
import com.kapirti.social_chat_food_video.R
import com.kapirti.social_chat_food_video.model.GetChatMessage
import com.kapirti.social_chat_food_video.model.User
import com.kapirti.social_chat_food_video.webrtc.ui.WebRtcActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Represents a reason why a shortcut should be pushed.
 */
enum class PushReason {
    IncomingMessage,
    OutgoingMessage,
}

/**
 * Handles all operations related to [Notification].
 */
@Singleton
class NotificationHelper @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        /**
         * The notification channel for messages. This is used for showing Bubbles.
         */
        private const val CHANNEL_NEW_MESSAGES = "new_messages"
        private const val CHANNEL_CALL = "calls"

        private const val REQUEST_CONTENT = 1
        private const val REQUEST_BUBBLE = 2
        private const val REQUEST_CALL = 3
    }

    private val appContext = context.applicationContext

    private val notificationManager: NotificationManager =
        context.getSystemService() ?: throw IllegalStateException()

    fun setUpNotificationChannels() {
        if (Build.VERSION.SDK_INT < 26) {
            return
        }
        if (notificationManager.getNotificationChannel(CHANNEL_NEW_MESSAGES) == null) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_NEW_MESSAGES,
                    "New messages", //appContext.getString(R.string.channel_new_messages),
                    // The importance must be IMPORTANCE_HIGH to show Bubbles.
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    //description = appContext.getString(R.string.channel_new_messages_description)
                    description = "All new incoming messages."
                },
            )
        }

        if (notificationManager.getNotificationChannel(CHANNEL_CALL) == null) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_CALL,
                    "Call records", //appContext.getString(R.string.channel_new_messages),
                    // The importance must be IMPORTANCE_HIGH to show Bubbles.
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    //description = appContext.getString(R.string.channel_new_messages_description)
                    description = "All new incoming calls."
                },
            )
        }
    }

    private fun User.toShortcut(
        additional: ShortcutInfoCompat.Builder.() -> ShortcutInfoCompat.Builder = { this },
    ): ShortcutInfoCompat {
//        val icon = IconCompat.createWithAdaptiveBitmap(
//            appContext.resources.assets.open(icon).use { input ->
//                BitmapFactory.decodeStream(input)
//            },
//        )
        return ShortcutInfoCompat.Builder(appContext, shortcutId)
            .setLocusId(LocusIdCompat(shortcutId))
            .setActivity(ComponentName(appContext, MainActivity::class.java))
            .setShortLabel(name)
            //.setIcon(icon)
            .setLongLived(true)
            .setCategories(hashSetOf("com.google.android.samples.socialite.category.SHARE_TARGET"))
            .setIntent(
                Intent(appContext, MainActivity::class.java)
                    .setAction(Intent.ACTION_VIEW)
                    .setData(contentUri),
            )
            .setPerson(
                Person.Builder()
                    .setName(name)
                    //.setIcon(icon)
                    .build(),
            )
            .additional()
            .build()
    }

    @WorkerThread
    fun pushShortcut(contact: User, reason: PushReason? = null) {
        ShortcutManagerCompat.pushDynamicShortcut(
            appContext,
            contact.toShortcut {
                when (reason) {
                    PushReason.IncomingMessage -> {
                        addCapabilityBinding("actions.intent.RECEIVE_MESSAGE")
                    }

                    PushReason.OutgoingMessage -> {
                        addCapabilityBinding("actions.intent.SEND_MESSAGE")
                    }

                    else -> this
                }
            },
        )
    }

    private fun flagUpdateCurrent(mutable: Boolean): Int {
        return if (mutable) {
            if (Build.VERSION.SDK_INT >= 31) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        }
    }

    @WorkerThread
    fun showNotification(
        contact: User,
        messages: List<GetChatMessage>,
        fromUser: Boolean,
        update: Boolean = false,
    ) {
        val icon = IconCompat.createWithAdaptiveBitmapContentUri(contact.iconUri)
        val user = Person.Builder().setName("You").build()
        val person = Person.Builder().setName(contact.name).setIcon(icon).build()

        val pendingIntent = PendingIntent.getActivity(
            appContext,
            REQUEST_BUBBLE,
            // Launch BubbleActivity as the expanded bubble.
            Intent(appContext, BubbleActivity::class.java)
                .setAction(Intent.ACTION_VIEW)
                .setData(contact.contentUri),
            flagUpdateCurrent(mutable = true),
        )

//            .setStyle(NotificationCompat.BigPictureStyle()
//                .bigPicture(bitmap) // Add the image here
//                .bigLargeIcon(null))
        val pictureStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(getBitmapFromURL(messages.first().mediaUri))

        // Let's add some more content to the notification in case it falls back to a normal
        // notification.
        val messagingStyle = NotificationCompat.MessagingStyle(user)
        val firstId = messages.first().id
        for (message in messages.reversed()) {
            val date = message.timestamp?.toDate() ?: Timestamp.now().toDate()
            val m = NotificationCompat.MessagingStyle.Message(
                message.text,
                //message.timestamp,
                date.time,
                //if (message.isIncoming) person else null,
                person,
            ).apply {
                if (message.mediaUri.isNotEmpty()) {
                    //setData(message.mediaMimeType, message.mediaUri.toUri())
                    android.util.Log.d("myTag","notification builder, media uri not null : ${message.mediaUri} and mime type : ${message.mediaMimeType}")
                    //setData("image/png", message.mediaUri.toUri())
                    //setData("image/jpeg", message.mediaUri.toUri())

                    setData("image/png", message.mediaUri.toUri())

                    //messagingStyle.(new NotificationCompat.BigPictureStyle().bigPicture(largeImage));

//                    val bitmap = getBitmapFromURL(message.mediaUri)
//                    bitmap?.let {
//                        setData("image/jpeg", it)
//                    }
                    //setData()
                }
            }
            if (message.id == firstId) {
                messagingStyle.addMessage(m)
            } else {
                messagingStyle.addHistoricMessage(m)
            }
        }

        val notificationStyle = if(messages.firstOrNull()?.mediaUri.isNullOrEmpty()) messagingStyle else pictureStyle
        android.util.Log.d("myTag5","notification style now is : $notificationStyle")

        val lastMessageDate = messages.last().timestamp?.toDate() ?: Timestamp.now().toDate()
        val builder = NotificationCompat.Builder(appContext, CHANNEL_NEW_MESSAGES)
            // A notification can be shown as a bubble by calling setBubbleMetadata()
            .setBubbleMetadata(
                NotificationCompat.BubbleMetadata.Builder(pendingIntent, icon)
                    // The height of the expanded bubble.
                    .setDesiredHeight(
                        appContext.resources.getDimensionPixelSize(R.dimen.bubble_height),
                    )
                    .apply {
                        // When the bubble is explicitly opened by the user, we can show the bubble
                        // automatically in the expanded state. This works only when the app is in
                        // the foreground.
                        if (fromUser) {
                            setAutoExpandBubble(true)
                        }
                        if (fromUser || update) {
                            setSuppressNotification(true)
                        }
                    }
                    .build(),
            )
            // The user can turn off the bubble in system settings. In that case, this notification
            // is shown as a normal notification instead of a bubble. Make sure that this
            // notification works as a normal notification as well.
            .setContentTitle(contact.name)
            .setSmallIcon(R.drawable.ic_message)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setShortcutId(contact.shortcutId)
            // This ID helps the intelligence services of the device to correlate this notification
            // with the corresponding dynamic shortcut.
            .setLocusId(LocusIdCompat(contact.shortcutId))
            .addPerson(person)
            .setShowWhen(true)
            // The content Intent is used when the user clicks on the "Open Content" icon button on
            // the expanded bubble, as well as when the fall-back notification is clicked.
            .setContentIntent(
                PendingIntent.getActivity(
                    appContext,
                    REQUEST_CONTENT,
                    Intent(appContext, MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contact.contentUri),
                    flagUpdateCurrent(mutable = false),
                ),
            )
            // Direct Reply
            .addAction(
                NotificationCompat.Action
                    .Builder(
                        IconCompat.createWithResource(appContext, R.drawable.ic_send),
                        //appContext.getString(R.string.label_reply),
                        "Reply",
                        PendingIntent.getBroadcast(
                            appContext,
                            REQUEST_CONTENT,
                            Intent(appContext, ReplyReceiver::class.java)
                                .setData(contact.contentUri),
                            flagUpdateCurrent(mutable = true),
                        ),
                    )
                    .addRemoteInput(
                        RemoteInput.Builder(ReplyReceiver.KEY_TEXT_REPLY)
                            //.setLabel(appContext.getString(R.string.hint_input))
                            .setLabel("Type a message…")
                            .build(),
                    )
                    .setAllowGeneratedReplies(true)
                    .build(),
            )
            // Let's add some more content to the notification in case it falls back to a normal
            // notification.
            .setStyle(notificationStyle)
            //.setWhen(messages.last().timestamp)
            .setWhen(lastMessageDate.time)
        // Don't sound/vibrate if an update to an existing notification.
        if (update) {
            builder.setOnlyAlertOnce(true)
        }
        notificationManager.notify(contact.id.hashCode(), builder.build())
    }

    fun getBitmapFromURL(strUrl: String) : Bitmap? {
        try {
            val url = URL(strUrl)
            val connection = url.openConnection()
            connection.doInput = true
            connection.connect()
            val input = connection.getInputStream()
            val myBitmap = BitmapFactory.decodeStream(input)
            return myBitmap
        } catch (e: Exception) {
            return null
        }
    }

    @WorkerThread
    fun showCallNotification(
        contact: User,
        //roomId: String
//        messages: List<ChatMessage>,
//        fromUser: Boolean,
//        update: Boolean = false,
    ) {
        val icon = IconCompat.createWithAdaptiveBitmapContentUri(contact.iconUri)
        val user = Person.Builder().setName("You").build()
        val person = Person.Builder().setName(contact.name).setIcon(icon).build()

        val pendingIntent = PendingIntent.getActivity(
            appContext,
            REQUEST_CALL,
            // Launch BubbleActivity as the expanded bubble.
            Intent(appContext, WebRtcActivity::class.java)
                .setAction(Intent.ACTION_VIEW)
                .putExtra("roomId", contact.id)
                .putExtra("receiver", true)
                .setData(contact.contentUri),
            flagUpdateCurrent(mutable = true),
        )

        val builder = NotificationCompat.Builder(appContext, CHANNEL_CALL)

            // The user can turn off the bubble in system settings. In that case, this notification
            // is shown as a normal notification instead of a bubble. Make sure that this
            // notification works as a normal notification as well.
            .setContentTitle(contact.name)
            .setSmallIcon(R.drawable.ic_message)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setShortcutId(contact.shortcutId)
            // This ID helps the intelligence services of the device to correlate this notification
            // with the corresponding dynamic shortcut.
            .setLocusId(LocusIdCompat(contact.shortcutId))
            .addPerson(person)
            .setShowWhen(true)
            // The content Intent is used when the user clicks on the "Open Content" icon button on
            // the expanded bubble, as well as when the fall-back notification is clicked.
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setUsesChronometer(false)
            .setColorized(true)
            .setStyle(
                NotificationCompat.CallStyle.forIncomingCall(
                    Person.Builder()
                        .setName("denemee")
                        .build(),
                    pendingIntent,
                    pendingIntent,
                )
            )
            .setFullScreenIntent(pendingIntent, true)

        notificationManager.notify(contact.id.hashCode(), builder.build())
    }

    fun dismissNotification(chatId: Long) {
        notificationManager.cancel(chatId.toInt())
    }

    fun canBubble(contact: User): Boolean {
        if (Build.VERSION.SDK_INT < 30) return false
        val channel = notificationManager.getNotificationChannel(
            CHANNEL_NEW_MESSAGES,
            contact.shortcutId,
        )
        return notificationManager.areBubblesEnabledCompat() || channel?.canBubble() == true
    }

    @RequiresApi(29)
    private fun NotificationManager.areBubblesEnabledCompat(): Boolean {
        return if (Build.VERSION.SDK_INT >= 31) {
            areBubblesEnabled()
        } else {
            @Suppress("DEPRECATION")
            areBubblesAllowed()
        }
    }
}
