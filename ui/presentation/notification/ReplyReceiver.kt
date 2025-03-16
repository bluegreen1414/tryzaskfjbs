package com.kapirti.social_chat_food_video.ui.presentation.notification

import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.kapirti.social_chat_food_video.model.SaveChatMessage
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReplyReceiver : BroadcastReceiver() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var firestoreService: FirestoreService

    companion object {
        const val KEY_TEXT_REPLY = "reply"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val results = RemoteInput.getResultsFromIntent(intent) ?: return
        // The message typed in the notification reply.
        val input = results.getCharSequence(KEY_TEXT_REPLY)?.toString()
        val uri = intent.data ?: return
        val chatId = uri.lastPathSegment ?: return

        if (!input.isNullOrBlank()) {
            val pendingResult = goAsync()
            val job = Job()
            CoroutineScope(job).launch {
                try {
                    //repository.sendMessage(chatId, input.toString(), null, null)
                    val myId = firebaseAuth.currentUser!!.uid
//
//                    val room = uiState.value.chatRoom
//                    room?.lastMessageTime = Timestamp.now()
//                    room?.lastMessageSenderId = myId
//                    room?.lastMessage = input
//                    room?.let {
//                        firestoreService.setChatRoom(room.id, room)

                    val chatMessage = SaveChatMessage(
                        senderId = myId,
                        text = input,
                       // chatId,  "", "", ,
                        timestamp = Timestamp.now())
                    firestoreService.getChatRoomMessageReference(chatId).add(chatMessage)
                        .addOnSuccessListener {

                        }


                    // We should update the notification so that the user can see that the reply has
                    // been sent.
                    //repository.updateNotification(chatId)

                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
