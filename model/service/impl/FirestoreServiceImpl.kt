package com.kapirti.social_chat_food_video.model.service.impl

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.snapshots
import com.kapirti.social_chat_food_video.model.User
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.trace
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.kapirti.social_chat_food_video.core.datastore.CountryRepository
import com.kapirti.social_chat_food_video.core.datastore.LearnLanguageRepository
import com.kapirti.social_chat_food_video.model.Block
import com.kapirti.social_chat_food_video.model.Cafe
import com.kapirti.social_chat_food_video.model.CallRecord
import com.kapirti.social_chat_food_video.model.ChatRoomFromChatRoom
import com.kapirti.social_chat_food_video.model.ChatRoomFromUserChat
import com.kapirti.social_chat_food_video.model.Delete
import com.kapirti.social_chat_food_video.model.Hotel
import com.kapirti.social_chat_food_video.model.Report
import com.kapirti.social_chat_food_video.model.Restaurant
import com.kapirti.social_chat_food_video.model.SaveChatMessage
import com.kapirti.social_chat_food_video.model.UserFlirt
import com.kapirti.social_chat_food_video.model.UserLP
import com.kapirti.social_chat_food_video.model.UserMarriage
import com.kapirti.social_chat_food_video.model.ExplorePhotoVideo
import com.kapirti.social_chat_food_video.ui.presentation.profile.edit.nickname.Nickname
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.asDeferred

class FirestoreServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService,
    private val countryRepository: CountryRepository,
    private val learnLanguageRepository: LearnLanguageRepository,
): FirestoreService {
    override suspend fun getUser(userId: String): User? = suspendCoroutine { cont ->
        userCollection().document(userId).get().addOnSuccessListener {
            val x = it.toObject(User::class.java)
            cont.resume(x)
        }
    }
    override suspend fun getChatRoomFromChatRoom(chatRoomId: String): ChatRoomFromChatRoom? = suspendCoroutine { cont ->
        chatRoomsCollection().document(chatRoomId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val x = it.result.toObject(ChatRoomFromChatRoom::class.java)
                cont.resume(x)
            } else {
                cont.resume(null)
            }
        }
    }
    override suspend fun getChatMessage(chatRoomId: String): Flow<List<SaveChatMessage>> {
        return getChatRoomMessageReference(chatRoomId).orderBy("timestamp", Query.Direction.DESCENDING).snapshots().map { snapshot -> snapshot.toObjects(SaveChatMessage::class.java) }
    }


    override suspend fun saveUser(user: User) { userCollection().document(user.id).set(user) }
    override suspend fun saveChatRoomFromChatRoom(chatRoomId: String, chatRoom: ChatRoomFromChatRoom) = suspendCoroutine { cont ->
        chatRoomsCollection().document(chatRoomId).set(chatRoom).addOnCompleteListener {
            cont.resume(it.isSuccessful)
        }
    }
    override suspend fun block(uid: String, partnerUid: String, block: Block): Unit = trace(SAVE_BLOCK_USER) { userBlockDocument(uid, partnerUid).set(block).await() }
    override suspend fun report(uid: String, partnerUid: String, report: Report): Unit = trace(SAVE_REPORT) { userReportDocument(uid = uid, partnerUid = partnerUid).set(report).await() }


    override suspend fun deleteUserChat(who: String, partnerId: String) { userChatCollection(who).document(partnerId).delete().await() }
    override suspend fun deleteChat(chatId: String) {
        val matchingChats = chatRoomsCollection().document(chatId).collection("chats").get().await()
        matchingChats.map { it.reference.delete().asDeferred() }.awaitAll()
    }



    /**    override suspend fun setUserOnline() {
        userCollection().document(auth.currentUserId).update("status", "online")
    }

    override suspend fun setUserOffline() {
        //userCollection().document(auth.currentUserId).update("status", Timestamp.now().toDate().time.toString())
        userCollection().document(auth.currentUserId).update("status", System.currentTimeMillis().toString())
        //userCollection().document(auth.currentUserId).update("status", Timestamp.now().seconds)
    }

    override suspend fun setUserTyping(typingRoomId: String) {
        userCollection().document(auth.currentUserId).update("typingTo", typingRoomId)
    }

    override suspend fun clearUserTyping(typingRoomId: String) {
        userCollection().document(auth.currentUserId).update("typingTo", "")
    }

    override suspend fun observeOtherChatState(userId: String): Flow<Pair<String, String>> {
        return userCollection().document(userId).snapshots().map { snapshot ->
            snapshot.getString("status").orEmpty() to snapshot.getString("typingTo").orEmpty()
        }

    }*/


    override suspend fun getChatRoomMessageReference(chatRoomId: String): CollectionReference {
        return chatRoomsCollection().document(chatRoomId).collection("chats")
    }

/**    //@OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getConversations(userId: String): Flow<List<ChatRoom>> {
        return chatRoomsCollection().whereArrayContains("userIds", userId)
            .orderBy("lastMessageTime", Query.Direction.DESCENDING)
            //.addSnapshotListener { value, error -> value.toObjects(ChatRoom::class.java) }
            .snapshotFlow().map { snapshot ->
                android.util.Log.d("myTag","snapshot update triggered..")
                snapshot.toObjects(ChatRoom::class.java)
            }
    }*/




    override fun updateUserFcmToken(token: String) {
        userCollection().document(auth.currentUserId).update(FCM_TOKEN_FIELD, token)
    }






  /**
    @OptIn(ExperimentalCoroutinesApi::class)
    override val currentUserConversations: Flow<List<ChatRoom>>
        get() = auth.currentUser.flatMapLatest { user ->
            chatRoomsCollection().whereArrayContains("userIds", user.id).orderBy("lastMessageTime", Query.Direction.DESCENDING).snapshots().map { snapshot -> snapshot.toObjects() } }




*/

  @OptIn(ExperimentalCoroutinesApi::class)
  override val userBlockUsers: Flow<List<Block>>
      get() = auth.currentUser.flatMapLatest { user ->
          userBlockUsersCollection().dataObjects() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val nicknames: Flow<List<Nickname>>
        get() = auth.currentUser.flatMapLatest { user ->
            nicknamesCollection().dataObjects() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val usersFlirt: Flow<List<UserFlirt>>
        get() = countryRepository.readCountry()
            .flatMapLatest { country -> userFlirtCollection(country).dataObjects() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val usersMarriage: Flow<List<UserMarriage>>
        get() = countryRepository.readCountry()
            .flatMapLatest { country -> userMarriageCollection(country).dataObjects() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val usersLP: Flow<List<UserLP>>
        get() = learnLanguageRepository.readLearnLanguageState()
            .flatMapLatest { ct -> userLanguagePracticeCollection(ct).dataObjects() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val restaurants: Flow<List<Restaurant>>
        get() = countryRepository.readCountry().flatMapLatest { country -> restaurantCollection(country).dataObjects() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val cafes: Flow<List<Cafe>>
        get() = countryRepository.readCountry().flatMapLatest { country -> cafeCollection(country).dataObjects() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val hotels: Flow<List<Hotel>>
        get() = countryRepository.readCountry().flatMapLatest { ct -> hotelCollection(ct).dataObjects() }


    @OptIn(ExperimentalCoroutinesApi::class)
    override val callRecords: Flow<List<CallRecord>>
        get() = auth.currentUser.flatMapLatest { ct -> userCallRecordCollection(ct.id).orderBy("callStart", Query.Direction.DESCENDING).dataObjects() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val userChats: Flow<List<ChatRoomFromUserChat>>
        get() = auth.currentUser.flatMapLatest { user ->
            userChatCollection(user.id).orderBy(LAST_MESSAGE_TIME_FIELD, Query.Direction.DESCENDING).dataObjects() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val explorePhotos: Flow<List<ExplorePhotoVideo>>
        get() = auth.currentUser.flatMapLatest { user ->
            explorePhotoCollection().orderBy(DATE_OF_CREATION_FIELD, Query.Direction.DESCENDING).dataObjects() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val explores: Flow<List<ExplorePhotoVideo>>
        get() = auth.currentUser.flatMapLatest { user ->
            exploreCollection().orderBy(DATE_OF_CREATION_FIELD, Query.Direction.DESCENDING).dataObjects() }



    override suspend fun saveCallRecord(id: String, callRecord: CallRecord): Unit = trace(SAVE_CALL_RECORD_TRACE){ userCallRecordCollection(id).add(callRecord) }
    override suspend fun saveChatRoomMessage(roomId: String, saveChatMessage: SaveChatMessage): Unit = trace(SAVE_CHAT_MESSAGE_TRACE) { getChatRoomMessageReference(roomId).add(saveChatMessage)}
    override suspend fun updateUserChatUnread(partnerId: String): Unit = trace(SAVE_UNREAD_TRACE){ userChatDocument(partnerId = partnerId, who = auth.currentUserId).update(UNREAD_FIELD, 0) }
    override suspend fun getUserFlirt(country: String, id: String): UserFlirt? = userFlirtDocument(country = country, id = id).get().await().toObject()
    override suspend fun getUserMarriage(country: String, id: String): UserMarriage? = userMarriageDocument(country = country, id = id).get().await().toObject()
    override suspend fun getUserLP(motherTongue: String, id: String): UserLP? = userLanguagePracticeDocument(motherTongue = motherTongue, id = id).get().await().toObject()
    override suspend fun getHotel(country: String, id: String): Hotel? = hotelDocument(country = country, id = id).get().await().toObject()
    override suspend fun getRestaurant(country: String, id: String): Restaurant? = restaurantDocument(country = country, id = id).get().await().toObject()
    override suspend fun getCafe(country: String, id: String): Cafe? = cafeDocument(country = country, id = id).get().await().toObject()
    override suspend fun getChatStatus(id: String): ChatRoomFromUserChat? = userChatDocument(who = auth.currentUserId, partnerId = id).get().await().toObject()
    override suspend fun deleteAccount(delete: Delete): Unit = trace(DELETE_ACCOUNT_TRACE) { deleteCollection().add(delete).await() }



    private fun userCollection(): CollectionReference = firestore.collection(USER_COLLECTION)
    private fun userDocument(id: String): DocumentReference = userCollection().document(id)
    private fun userChatCollection(uid: String): CollectionReference = userDocument(uid).collection(CHAT_COLLECTION)
    private fun userChatDocument(who: String, partnerId: String): DocumentReference = userChatCollection(who).document(partnerId)
    private fun userBlockCollection(uid: String): CollectionReference = userDocument(uid).collection(BLOCK_COLLECTION)
    private fun userBlockDocument(uid: String, partnerUid: String): DocumentReference = userBlockCollection(uid).document(partnerUid)
    private fun userReportDocument(uid: String, partnerUid: String): DocumentReference = userDocument(partnerUid).collection(REPORT_COLLECTION).document(uid)
    private fun userCallRecordCollection(uid: String): CollectionReference = userDocument(uid).collection(CALL_RECORDS_COLLECTION)
    private fun userBlockUsersCollection(): CollectionReference = userDocument(auth.currentUserId).collection(BLOCK_COLLECTION)
    private fun nicknamesCollection(): CollectionReference = firestore.collection(NICKNAME_COLLECTION)
    private fun exploreCollection(): CollectionReference = firestore.collection(EXPLORE_COLLECTION)
    private fun explorePhotoCollection(): CollectionReference = firestore.collection(EXPLORE_PHOTO_COLLECTION)

    private fun chatRoomsCollection(): CollectionReference = firestore.collection(CHATROOM_COLLECTION)
    //private fun callRecordsCollection(): CollectionReference = firestore.collection(CALL_RECORDS_COLLECTION)
    private fun userFlirtCollection(country: String): CollectionReference = firestore.collection(FLIRT_COLLECTION).document(country).collection(country)
    private fun userFlirtDocument(country: String, id: String): DocumentReference = userFlirtCollection(country).document(id)
    private fun userMarriageCollection(country: String): CollectionReference = firestore.collection(MARRIAGE_COLLECTION).document(country).collection(country)
    private fun userMarriageDocument(country: String, id: String): DocumentReference = userMarriageCollection(country).document(id)
    private fun userLanguagePracticeCollection(motherTongue: String): CollectionReference = firestore.collection(LANGUAGE_PRACTICE_COLLECTION).document(motherTongue).collection(motherTongue)
    private fun userLanguagePracticeDocument(motherTongue: String, id: String): DocumentReference = userLanguagePracticeCollection(motherTongue).document(id)
    private fun hotelCollection(country: String): CollectionReference = firestore.collection(HOTEL_COLLECTION).document(country).collection(HOTEL_COLLECTION)
    private fun hotelDocument(country: String, id: String): DocumentReference = hotelCollection(country).document(id)
    private fun restaurantCollection(country: String): CollectionReference = firestore.collection(RESTAURANT_COLLECTION).document(country).collection(RESTAURANT_COLLECTION)
    private fun restaurantDocument(country: String, id: String): DocumentReference = restaurantCollection(country).document(id)
    private fun cafeCollection(country: String): CollectionReference = firestore.collection(CAFE_COLLECTION).document(country).collection(CAFE_COLLECTION)
    private fun cafeDocument(country: String, id: String): DocumentReference = cafeCollection(country).document(id)
    private fun deleteCollection(): CollectionReference = firestore.collection(DELETE_COLLECTION)


    fun Query.snapshotFlow(): Flow<QuerySnapshot> = callbackFlow {
        val listenerRegistration = addSnapshotListener { value, error ->
            if (error != null) {
                android.util.Log.d("myTag2","close it!, error is $error")
                close()
                return@addSnapshotListener
            }
            if (value != null) {
                android.util.Log.d("myTag2","send it!")
                trySend(value)
            }
        }
        awaitClose {
            android.util.Log.d("myTag2","remove it!")
            listenerRegistration.remove()
        }
    }


    companion object {
        const val EXPLORE_COLLECTION = "Explore"
        const val FLIRT_COLLECTION = "Flirt"
        const val MARRIAGE_COLLECTION = "Marriage"
        const val LANGUAGE_PRACTICE_COLLECTION = "LanguagePractice"
        const val RESTAURANT_COLLECTION = "Restaurant"
        const val CAFE_COLLECTION = "Cafe"
        const val HOTEL_COLLECTION = "Hotel"
        const val ONLINE_FIELD = "online"
        const val LAST_SEEN_FIELD = "lastSeen"
        const val GENDER_FIELD = "gender"
        const val HOBBY_FIELD = "hobby"
        const val PHOTO_FIELD = "photo"
        const val DESCRIPTION_FIELD = "description"
        const val USERNAME_FIELD = "username"
        const val BIRTHDAY_FIELD = "birthday"
        const val CITY_FIELD = "city"
        const val ADDRESS_FIELD = "address"
        const val TYPE_FIELD = "type"
        const val COUNTRY_FIELD = "country"
        const val LEARN_LANGUAGE_FIELD = "learnLanguage"
        const val MOTHER_TONGUE_FIELD = "motherTongue"
        const val FCM_TOKEN_FIELD = "fcmToken"
        const val CHAT_COLLECTION = "Chat"
        private const val BLOCK_COLLECTION = "Block"
        private const val REPORT_COLLECTION = "Report"
        private const val SAVE_CALL_RECORD_TRACE = "saveCallRecord"
        const val UNREAD_FIELD = "unread"
        const val TYPING_FIELD = "isTyping"
        const val STORY_COLLECTION = "Story"
        const val LAST_MESSAGE_FIELD = "lastMessage"
        const val LAST_MESSAGE_SENDER_ID_FIELD = "lastMessageSenderId"
        const val LAST_MESSAGE_TIME_FIELD = "lastMessageTime"
        private const val SAVE_UNREAD_TRACE = "saveUnread"
        private const val NICKNAME_COLLECTION = "nicknames"
        private const val SAVE_ACCOUNT_CHAT_TRACE = "saveAccountChat"
        private const val SAVE_ACCOUNT_ARCHIVE_TRACE = "saveAccountArchive"
        private const val SAVE_CHAT_MESSAGE_TRACE = "saveChatMessage"
        private const val SAVE_BLOCK_USER = "saveBlockUser"
        private const val SAVE_REPORT = "saveReport"
        const val EXPLORE_PHOTO_COLLECTION = "ExplorePhoto"
        private const val DELETE_COLLECTION = "Delete"
        private const val DELETE_ACCOUNT_TRACE = "deleteAccount"

        private const val CALL_RECORDS_COLLECTION = "CallRecords"
        const val CHATROOM_COLLECTION = "ChatRooms"
        const val USER_COLLECTION = "User"
        private const val DATE_OF_CREATION_FIELD = "dateOfCreation"

        fun getChatRoomId(firstUserId: String, secondUserId: String) : String {
            return if(firstUserId.hashCode() < secondUserId.hashCode()) {
                firstUserId + "_" + secondUserId
            } else {
                secondUserId + "_" + firstUserId
            }
        }
    }
}
