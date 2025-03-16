package com.kapirti.social_chat_food_video.ui.presentation.blockedusers

import com.kapirti.social_chat_food_video.common.stateInUi
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel

@HiltViewModel
class BlockedUsersViewModel @Inject constructor(
    private val firestoreService: FirestoreService,
    logService: LogService,
): KapirtiViewModel(logService) {
    val blockedUsers = firestoreService.userBlockUsers.stateInUi(emptyList())
}

/**
    fun onArchiveSwipe(chat: Chat){
        launchCatching{
            firestoreService.saveUserArchive(uid = accountService.currentUserId, chat = chat, chatId = chat.chatId)
            firestoreService.deleteUserChat(uid = accountService.currentUserId, chatId = chat.chatId)
        }
    }




    private var job: Job? = null

    fun onChatsClick(chat: Chat) {
        launchCatching {
            if (accountService.hasUser) {
                job?.cancel()
                job = viewModelScope.launch {
                    try {
                        val response = chatIdRepository.saveChatIdState(chat.chatId)
                    } catch (e: FirebaseFirestoreException) {
                        println("raheem: ${e.message}")
                    }
                }
            }
        }
    }
}
*/
