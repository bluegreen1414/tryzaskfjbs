package com.kapirti.social_chat_food_video.ui.presentation.home.users

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kapirti.social_chat_food_video.common.stateInUi
import com.kapirti.social_chat_food_video.core.constants.EditType
import com.kapirti.social_chat_food_video.core.constants.SelectType
import com.kapirti.social_chat_food_video.core.datastore.CameraTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.kapirti.social_chat_food_video.core.datastore.CountryRepository
import com.kapirti.social_chat_food_video.core.datastore.EditTypeRepository
import com.kapirti.social_chat_food_video.core.datastore.LearnLanguageRepository
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import com.kapirti.social_chat_food_video.ui.presentation.settings.SettingsUiState
import com.kapirti.social_chat_food_video.ui.presentation.story.Story
import kotlinx.coroutines.flow.map

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val accountService: AccountService,
    private val firestoreService: FirestoreService,
    private val countryRepository: CountryRepository,
    private val lPLanguageRepository: LearnLanguageRepository,
    private val cameraTypeRepository: CameraTypeRepository,
    logService: LogService,
): KapirtiViewModel(logService) {
    val uiState = accountService.currentUser.map { SettingsUiState(it.isAnonymous) }
    var allStories by mutableStateOf<List<Story>>(emptyList())
    var myStory by mutableStateOf<Story?>(null)

    val usersFlirt = firestoreService.usersFlirt.stateInUi(emptyList())
    val usersMarriage = firestoreService.usersMarriage.stateInUi(emptyList())
    val usersLP = firestoreService.usersLP.stateInUi(emptyList())
    val cafes = firestoreService.cafes.stateInUi(emptyList())
    val restaurants = firestoreService.restaurants.stateInUi(emptyList())
    val hotels = firestoreService.hotels.stateInUi(emptyList())


    val currentUserId = accountService.currentUserId

    private val _country = mutableStateOf<String?>(null)
    val country: String?
        get() = _country.value

    private val _language = mutableStateOf<String?>(null)
    val language: String?
        get() = _language.value

    private val _photo = mutableStateOf<String?>(null)
    val photo: String?
        get() = _photo.value

    private val _username = mutableStateOf<String?>(null)
    val username: String?
        get() = _username.value

    private val ff = FirebaseFirestore.getInstance()

    init {
        launchCatching {
            countryRepository.readCountry().collect { itCountry ->
                _country.value = itCountry
                lPLanguageRepository.readLearnLanguageState().collect { itCountry ->
                    _language.value = itCountry

                    val newList = mutableListOf<Story>()
                    ff.collection(FirestoreServiceImpl.STORY_COLLECTION).get().addOnCompleteListener {
                        if (it.isSuccessful && it.result != null) {
                            val x = it.result.toObjects(Story::class.java)

                            for (i in x) {
                                newList.add(i)
                            }
                            newList.sortByDescending { it.dateOfCreation }
                            allStories = newList
                        }
                    }

                    ff.collection(FirestoreServiceImpl.STORY_COLLECTION).document(currentUserId).get().addOnCompleteListener {
                        if (it.isSuccessful && it.result != null) {
                            val x = it.result.toObject(Story::class.java)
                            myStory = x
                        }
                    }
                    firestoreService.getUser(accountService.currentUserId)?.let {
                        when (it.type) {
                            SelectType.FLIRT -> {
                                firestoreService.getUserFlirt(country = it.country, id = it.id)
                                    ?.let { itUserFlirt ->
                                        _photo.value = itUserFlirt.photo
                                        _username.value = itUserFlirt.username
                                    }
                            }

                            SelectType.MARRIAGE -> {
                                firestoreService.getUserMarriage(country = it.country, id = it.id)
                                    ?.let { itUserMarriage ->
                                        _photo.value = itUserMarriage.photo
                                        _username.value = itUserMarriage.username
                                    }
                            }

                            SelectType.LANGUAGE_PRACTICE -> {
                                firestoreService.getUserLP(motherTongue = it.motherTongue, id = it.id)
                                    ?.let { itLP ->
                                        _photo.value = itLP.photo
                                        _username.value = itLP.username
                                    }
                            }

                            SelectType.HOTEL -> {
                                firestoreService.getHotel(country = it.country, id = it.id)
                                    ?.let { itHotel ->
                                        _photo.value = itHotel.photo
                                        _username.value = itHotel.username
                                    }
                            }

                            SelectType.RESTAURANT -> {
                                firestoreService.getRestaurant(country = it.country, id = it.id)
                                    ?.let { itRestaurant ->
                                        _photo.value = itRestaurant.photo
                                        _username.value = itRestaurant.username
                                    }
                            }

                            SelectType.CAFE -> {
                                firestoreService.getCafe(country = it.country, id = it.id)
                                    ?.let { itCafe ->
                                        _photo.value = itCafe.photo
                                        _username.value = itCafe.username
                                    }
                            }
                            SelectType.NOPE -> {}
                        }
                    }
                }
            }
        }
    }

    fun onLangClick(){
        launchCatching {
            //editTypeRepository.saveEditTypeState(LEARN_LANGUAGE)
        }
    }
    fun onCountryClick(){
        launchCatching {
            //editTypeRepository.saveEditTypeState(EditType.COUNTRY)
        }
    }
    fun onAddClick(onAddClick: () -> Unit){
        launchCatching {
            cameraTypeRepository.saveState(FirestoreServiceImpl.STORY_COLLECTION)
            onAddClick()
        }
    }
}
