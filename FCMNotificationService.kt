package com.kapirti.social_chat_food_video

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kapirti.social_chat_food_video.model.GetChatMessage
import com.kapirti.social_chat_food_video.model.User
import com.kapirti.social_chat_food_video.ui.presentation.notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //TODO : Update fcmToken in Firestore
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        android.util.Log.d("myTag","FCM message received")
        android.util.Log.d("myTag","message from : ${message.from}")

        // Check if message contains a data payload.
        if (message.data.isNotEmpty()) {

            val mediaUri : String? = message.data.get("mediaUri")
            val mediaMimeType : String? = message.data.get("mediaMimeType")
            val mediaTempText = "$mediaMimeType : $mediaUri"

            android.util.Log.d("myTag", "Message data payload: ${message.data}")
            android.util.Log.d("myTag", "Message data payload sender: ${message.data.get("theSender")}")
            android.util.Log.d("myTag", "Message data payload media uri: $mediaUri")
            android.util.Log.d("myTag", "Message data payload media mime type: $mediaMimeType")
            android.util.Log.d("myTag", "Message data payload,, is call?? : ${message.data.get("isCall")}")

            // Check if data needs to be processed by long running job
//            if (needsToBeScheduled()) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob()
//            } else {
//                // Handle message within 10 seconds
//                handleNow()
//            }
        }

        // Check if message contains a notification payload.
        message.notification?.let {

            val mediaUri : String? = message.data.get("mediaUri")
            val mediaMimeType : String? = message.data.get("mediaMimeType")
            val mediaTempText = "$mediaMimeType : $mediaUri"

            android.util.Log.d("myTag", "NOTIFICATION CHANNEL ID is: ${it.channelId}")


            android.util.Log.d("myTag", "Message Notification Body: ${it.body}")

            val isItCall = message.data.get("isCall").toBoolean()
            if (isItCall) {
                notificationHelper.showCallNotification(
                    User(
                        id = message.data.get("theSender").orEmpty(),
                        name = it.title.orEmpty(),
                    ),
                    //listOf(ChatMessage(message = it.body!!)), true,
                )
            } else {
                android.util.Log.d("myTag3","image url from itself : ${it.imageUrl}")
                android.util.Log.d("myTag3","image url passed/read directly : ${mediaUri}")
                //if (mediaUri.isNullOrEmpty()) {
                    notificationHelper.showNotification(
                        User(
                            id = message.data.get("theSender").orEmpty(),
                            name = it.title.orEmpty(),
                        ),
                        listOf(
                            GetChatMessage(
                                //message = it.body ?: mediaTempText)
                                text = it.body.orEmpty(),
                                mediaUri = mediaUri.orEmpty(),
                                //mediaUri = it.imageUrl.toString(),
                                mediaMimeType = mediaMimeType.orEmpty()
                            )
                        ),
                        true,
                    )
                //} else {
                //
                //}
            }


        }

        //val userId = getIntent().getExtras().getString("userId")

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.



        //notificationHelper.showNotification()

    }

}
