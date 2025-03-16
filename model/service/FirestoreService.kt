package com.kapirti.social_chat_food_video.model.service

import com.google.firebase.firestore.CollectionReference
import com.kapirti.social_chat_food_video.model.Block
import com.kapirti.social_chat_food_video.model.Cafe
import com.kapirti.social_chat_food_video.model.CallRecord
import com.kapirti.social_chat_food_video.model.ChatRoomFromChatRoom
import com.kapirti.social_chat_food_video.model.ChatRoomFromUserChat
import com.kapirti.social_chat_food_video.model.Delete
import com.kapirti.social_chat_food_video.model.ExplorePhotoVideo
import com.kapirti.social_chat_food_video.model.Hotel
import com.kapirti.social_chat_food_video.model.Report
import com.kapirti.social_chat_food_video.model.Restaurant
import com.kapirti.social_chat_food_video.model.SaveChatMessage
import com.kapirti.social_chat_food_video.model.User
import com.kapirti.social_chat_food_video.model.UserFlirt
import com.kapirti.social_chat_food_video.model.UserLP
import com.kapirti.social_chat_food_video.model.UserMarriage
import com.kapirti.social_chat_food_video.ui.presentation.profile.edit.nickname.Nickname
import kotlinx.coroutines.flow.Flow


interface FirestoreService {
    val userChats: Flow<List<ChatRoomFromUserChat>>

    suspend fun getUser(userId: String): User?
    suspend fun getChatRoomFromChatRoom(chatRoomId: String): ChatRoomFromChatRoom?
    suspend fun getChatMessage(chatRoomId: String) : Flow<List<SaveChatMessage>>

    suspend fun saveUser(user: User)
    suspend fun saveChatRoomFromChatRoom(chatRoomId: String, chatRoom: ChatRoomFromChatRoom) : Boolean
    suspend fun block(uid: String, partnerUid: String, block: Block)
    suspend fun report(uid: String, partnerUid: String, report: Report)

    suspend fun deleteChat(chatId: String)
    suspend fun deleteUserChat(who: String, partnerId: String)


    //  suspend fun setUserOnline()

//    suspend fun setUserOffline()

//    suspend fun setUserTyping(typingRoomId: String)

//    suspend fun clearUserTyping(typingRoomId: String)

//    suspend fun observeOtherChatState(userId: String) : Flow<Pair<String, String>>

//    val currentUserConversations: Flow<List<ChatRoom>>

    suspend fun getChatRoomMessageReference(chatRoomId: String) : CollectionReference

//    suspend fun getConversations(userId: String) : Flow<List<ChatRoom>>

    fun updateUserFcmToken(token: String)

    val usersFlirt: Flow<List<UserFlirt>>
    val usersMarriage: Flow<List<UserMarriage>>
    val usersLP: Flow<List<UserLP>>
    val hotels: Flow<List<Hotel>>
    val restaurants: Flow<List<Restaurant>>
    val cafes: Flow<List<Cafe>>
    val callRecords: Flow<List<CallRecord>>
    val nicknames: Flow<List<Nickname>>
    val userBlockUsers: Flow<List<Block>>
    val explores: Flow<List<ExplorePhotoVideo>>
    val explorePhotos: Flow<List<ExplorePhotoVideo>>
    suspend fun saveCallRecord(id: String, callRecord: CallRecord)
    suspend fun saveChatRoomMessage(roomId: String, saveChatMessage: SaveChatMessage)
    suspend fun updateUserChatUnread(partnerId: String)
    suspend fun getUserFlirt(country: String, id: String): UserFlirt?
    suspend fun getUserMarriage(country: String, id: String): UserMarriage?
    suspend fun getUserLP(motherTongue: String, id: String): UserLP?
    suspend fun getHotel(country: String, id: String): Hotel?
    suspend fun getRestaurant(country: String, id: String): Restaurant?
    suspend fun getCafe(country: String, id: String): Cafe?
    suspend fun getChatStatus(id: String): ChatRoomFromUserChat?
    suspend fun deleteAccount(delete: Delete)
}
