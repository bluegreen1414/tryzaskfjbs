package com.kapirti.social_chat_food_video.ui.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kapirti.social_chat_food_video.core.constants.SelectType
import com.kapirti.social_chat_food_video.core.datastore.CameraTypeRepository
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import com.kapirti.social_chat_food_video.model.ExploreUserItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestoreService: FirestoreService,
    private val accountService: AccountService,
    private val cameraTypeRepository: CameraTypeRepository,
    logService: LogService,
) : KapirtiViewModel(logService) {
    var media by mutableStateOf<List<ExploreUserItem>>(emptyList())
    private val ff = FirebaseFirestore.getInstance()
    val myId = accountService.currentUserId

    private val _accountType = mutableStateOf<String?>(null)
    val accountType: String?
        get() = _accountType.value

    private val _photo = mutableStateOf<String?>(null)
    val photo: String?
        get() = _photo.value

    private val _displayName = mutableStateOf<String?>(null)
    val displayName: String?
        get() = _displayName.value

    private val _bio = mutableStateOf<String?>(null)
    val bio: String?
        get() = _bio.value

    private val _username = mutableStateOf<String?>(null)
    val username: String?
        get() = _username.value

    private val _gender = mutableStateOf<String?>(null)
    val gender: String?
        get() = _gender.value

    private val _birthday = mutableStateOf<String?>(null)
    val birthday: String?
        get() = _birthday.value

    private val _learnLanguage = mutableStateOf<String?>(null)
    val learnLanguage: String?
        get() = _learnLanguage.value

    private val _motherTongue = mutableStateOf<String?>(null)
    val motherTongue: String?
        get() = _motherTongue.value

    init {
        launchCatching {
            val newList = mutableListOf<ExploreUserItem>()
            ff.collection(FirestoreServiceImpl.USER_COLLECTION).document(accountService.currentUserId)
                .collection(FirestoreServiceImpl.EXPLORE_COLLECTION).get().addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val x = it.result.toObjects(ExploreUserItem::class.java)

                    for (i in x){
                        newList.add(i)
                    }
                    newList.sortByDescending { it.dateOfCreation }
                    media = newList

                }
            }

            firestoreService.getUser(accountService.currentUserId)?.let {
                _accountType.value = it.type

                when (it.type) {
                    SelectType.FLIRT -> {
                        firestoreService.getUserFlirt(country = it.country, id = it.id)
                            ?.let { itUserFlirt ->
                                _bio.value = itUserFlirt.description
                                _photo.value = itUserFlirt.photo
                                _displayName.value = itUserFlirt.displayName
                                _username.value = itUserFlirt.username
                                _gender.value = itUserFlirt.gender
                                _birthday.value = itUserFlirt.birthday
                            }
                    }

                    SelectType.MARRIAGE -> {
                        firestoreService.getUserMarriage(country = it.country, id = it.id)
                            ?.let { itUserMarriage ->
                                _bio.value = itUserMarriage.description
                                _photo.value = itUserMarriage.photo
                                _displayName.value = itUserMarriage.displayName
                                _username.value = itUserMarriage.username
                                _gender.value = itUserMarriage.gender
                                _birthday.value = itUserMarriage.birthday
                            }
                    }

                    SelectType.LANGUAGE_PRACTICE -> {
                        firestoreService.getUserLP(motherTongue = it.motherTongue, id = it.id)
                            ?.let { itLP ->
                                _bio.value = itLP.description
                                _photo.value = itLP.photo
                                _displayName.value = itLP.displayName
                                _username.value = itLP.username
                                _gender.value = itLP.gender
                                _birthday.value = itLP.birthday
                                _learnLanguage.value = itLP.learnLanguage
                                _motherTongue.value = itLP.motherTongue
                            }
                    }

                    SelectType.HOTEL -> {
                        firestoreService.getHotel(country = it.country, id = it.id)
                            ?.let { itHotel ->
                                _bio.value = itHotel.description
                                _photo.value = itHotel.photo
                                _displayName.value = itHotel.displayName
                                _username.value = itHotel.username
                            }
                    }

                    SelectType.RESTAURANT -> {
                        firestoreService.getRestaurant(country = it.country, id = it.id)
                            ?.let { itRestaurant ->
                                _bio.value = itRestaurant.description
                                _photo.value = itRestaurant.photo
                                _displayName.value = itRestaurant.displayName
                                _username.value = itRestaurant.username
                            }
                    }

                    SelectType.CAFE -> {
                        firestoreService.getCafe(country = it.country, id = it.id)
                            ?.let { itCafe ->
                                _bio.value = itCafe.description
                                _photo.value = itCafe.photo
                                _displayName.value = itCafe.displayName
                                _username.value = itCafe.username
                            }
                    }
                    SelectType.NOPE -> {}
                }
            }
        }
    }

    fun onAddClick(onAddClick: () -> Unit){
        launchCatching {
            cameraTypeRepository.saveState(FirestoreServiceImpl.EXPLORE_COLLECTION)
            onAddClick()
        }
    }
}





/**

    fun saveAccountId() {
        launchCatching {
            accountIdRepository.saveAccountIdState(accountService.currentUserId)
        }
    }

    fun onSelectAccountTypeClicked(navigateEdit: () -> Unit) {
        launchCatching {
            editTypeRepository.saveEditTypeState(SELECT_ACCOUNT_TYPE)
            navigateEdit()
        }
    }



}

*/


/**

    fun onDisplayNameClick(navigateEdit: () -> Unit){
        launchCatching {
            editTypeRepository.saveEditTypeState(DISPLAY_NAME)
            navigateEdit()
        }
    }
    fun onNameSurnameClick(navigateEdit: () -> Unit){
        launchCatching {
            editTypeRepository.saveEditTypeState(NAME_SURNAME)
            navigateEdit()
        }
    }
    fun onGenderClick(navigateEdit: () -> Unit){
        launchCatching {
            editTypeRepository.saveEditTypeState(GENDER)
            navigateEdit()
        }
    }
    fun onBirthdayClick(navigateEdit: () -> Unit){
        launchCatching {
           // editTypeRepository.saveEditTypeState(BIRTHDAY)
           // navigateEdit()
        }
    }
    fun onDescriptionClick(navigateEdit: () -> Unit){
        launchCatching {
            editTypeRepository.saveEditTypeState(DESCRIPTION)
            navigateEdit()
        }
    }
}
*/



/**

data class SelectedUserPhotosUiState(
    val items: List<com.kapirti.video_food_delivery_shopping.model.UserPhotos> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
)

    private val editTypeRepository: EditTypeRepository,
    logService: LogService
) : ZepiViewModel(logService) {
    val uid = accountService.currentUserId


    private val _userMessagePhotos: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoadingPhotos = MutableStateFlow(false)
    private val _isTaskDeletedPhotos = MutableStateFlow(false)
    private val _taskAsyncPhotos = firestoreService.userPhotos
        .map { handleUserPhotos(it) }
        .catch { emit(Async.Error(AppText.loading_user_photos_error)) }

    val selectedPhotos: StateFlow<SelectedUserPhotosUiState> = combine(
        _userMessagePhotos, _isLoadingPhotos, _isTaskDeletedPhotos, _taskAsyncPhotos
    ) { userMessage, isLoading, isTaskDeleted, taskAsync ->
        when (taskAsync) {
            Async.Loading -> {
                SelectedUserPhotosUiState(isLoading = true)
            }

            is Async.Error -> {
                SelectedUserPhotosUiState(
                    userMessage = taskAsync.errorMessage,
                )
            }

            is Async.Success -> {
                SelectedUserPhotosUiState(
                    items = taskAsync.data,
                    isLoading = isLoading,
                    userMessage = userMessage,
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = SelectedUserPhotosUiState(isLoading = true)
        )


    private fun handleUserPhotos(task: List<com.kapirti.video_food_delivery_shopping.model.UserPhotos>?): Async<List<com.kapirti.video_food_delivery_shopping.model.UserPhotos>> {
        if (task == null) {
            return Async.Error(AppText.user_photos_not_found)
        }
        return Async.Success(task)
    }

    fun refresh() { launchCatching {  } }

    fun onAddClick(navigateEdit: () -> Unit){
        launchCatching {
            editTypeRepository.saveEditTypeState(PHOTO)
            navigateEdit()
        }
    }
}
  /*

    private val _userMessageAsset: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoadingAsset = MutableStateFlow(false)
    private val _isTaskDeletedAsset = MutableStateFlow(false)
    private val _taskAsyncAsset = firestoreService.myAssets
        .map { handleTask(it) }
        .catch { emit(Async.Error(AppText.loading_assets_error)) }

    val selectedAsset: StateFlow<SelectedAssetUiState> = combine(
        _userMessageAsset, _isLoadingAsset, _isTaskDeletedAsset, _taskAsyncAsset
    ) { userMessage, isLoading, isTaskDeleted, taskAsync ->
        when (taskAsync) {
            Async.Loading -> {
                SelectedAssetUiState(isLoading = true)
            }

            is Async.Error -> {
                SelectedAssetUiState(
                    userMessage = taskAsync.errorMessage,
                )
            }

            is Async.Success -> {
                SelectedAssetUiState(
                    items = taskAsync.data,
                    isLoading = isLoading,
                    userMessage = userMessage,
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = SelectedAssetUiState(isLoading = true)
        )

    private fun handleTask(task: List<Asset>?): Async<List<Asset>> {
    if (task == null) {
    return Async.Error(AppText.assets_not_found)
    }
    return Async.Success(task)
    }
    fun refresh() {
    launchCatching {  }
    }

}



    private val topics by lazy {
        listOf(
            InterestSection("Android", listOf("Jetpack Compose", "Kotlin", "Jetpack")),
            InterestSection(
                "Programming",
                listOf("Kotlin", "Declarative UIs", "Java", "Unidirectional Data Flow", "C++")
            ),
            InterestSection("Technology", listOf("Pixel", "Google"))
        )
    }

    private val people by lazy {
        listOf(
            "Kobalt Toral",
            "K'Kola Uvarek",
            "Kris Vriloc",
            "Grala Valdyr",
            "Kruel Valaxar",
            "L'Elij Venonn",
            "Kraag Solazarn",
            "Tava Targesh",
            "Kemarrin Muuda"
        )
    }



    // UI state exposed to the UI   Favorites(R.string.interests_section_topics),
    //    Assets(R.string.interests_section_people),
    private val _uiState = MutableStateFlow(InterestsUiState(loading = true))
    val uiState: StateFlow<InterestsUiState> = _uiState.asStateFlow()





    init {
        refreshAll()
    }

 /**   fun toggleTopicSelection(topic: TopicSelection) {
        viewModelScope.launch {
            interestsRepository.toggleTopicSelection(topic)
        }
    }

    fun togglePersonSelected(person: String) {
        viewModelScope.launch {
            interestsRepository.togglePersonSelected(person)
        }
    }*/


    private fun refreshAll() {
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            // Trigger repository requests in parallel
            /**       val topicsDeferred = async { interestsRepository.getTopics() }
            val peopleDeferred = async { interestsRepository.getPeople() }
            val publicationsDeferred = async { interestsRepository.getPublications() }

            // Wait for all requests to finish
            val topics = topicsDeferred.await().successOr(emptyList())
            val people = peopleDeferred.await().successOr(emptyList())
            val publications = publicationsDeferred.await().successOr(emptyList())*/

            _uiState.update {
                it.copy(
                    loading = false,
                    topics = topics,
                    people = people,
                )
            }
        }
    }
}
    */
*/
