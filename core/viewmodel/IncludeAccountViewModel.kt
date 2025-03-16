package com.kapirti.social_chat_food_video.core.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kapirti.social_chat_food_video.core.constants.SelectType.CAFE
import com.kapirti.social_chat_food_video.core.constants.SelectType.HOTEL
import com.kapirti.social_chat_food_video.core.constants.SelectType.LANGUAGE_PRACTICE
import com.kapirti.social_chat_food_video.core.constants.SelectType.RESTAURANT
import com.kapirti.social_chat_food_video.model.Cafe
import com.kapirti.social_chat_food_video.model.Hotel
import com.kapirti.social_chat_food_video.model.Restaurant
import com.kapirti.social_chat_food_video.model.UserFlirt
import com.kapirti.social_chat_food_video.model.UserLP
import com.kapirti.social_chat_food_video.model.UserMarriage

class IncludeAccountViewModel: ViewModel() {
    private val _accountType = mutableStateOf<String?>(null)
    val accountType: String?
        get() = _accountType.value

    private val _uid = mutableStateOf<String?>(null)
    val uid: String?
        get() = _uid.value

    private val _online = mutableStateOf<Boolean?>(null)
    val online: Boolean?
        get() = _online.value

    private val _displayName = mutableStateOf<String?>(null)
    val displayName: String?
        get() = _displayName.value

    private val _username = mutableStateOf<String?>(null)
    val username: String?
        get() = _username.value

    private val _profileImageUrl = mutableStateOf<String?>(null)
    val profileImageUrl: String?
        get() = _profileImageUrl.value

    private val _bio = mutableStateOf<String?>(null)
    val bio: String?
        get() = _bio.value

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

    private val _address = mutableStateOf<String?>(null)
    val address: String?
        get() = _address.value




    fun addFlirt(userFlirt: UserFlirt){
        allUser(
            nvGender = userFlirt.gender,
            nvBirthday = userFlirt.birthday,
            nvAccountType = LANGUAGE_PRACTICE,
            nvUid = userFlirt.id,
            nvDisplayName = userFlirt.displayName,
            nvBio = userFlirt.description,
            nvUsername = userFlirt.username,
            nvProfileImageUrl = userFlirt.photo,
            nvOnline = userFlirt.online
        )
    }
    fun addMarriage(userMarriage: UserMarriage){
        allUser(
            nvGender = userMarriage.gender,
            nvBirthday = userMarriage.birthday,
            nvAccountType = LANGUAGE_PRACTICE,
            nvUid = userMarriage.id,
            nvDisplayName = userMarriage.displayName,
            nvBio = userMarriage.description,
            nvUsername = userMarriage.username,
            nvProfileImageUrl = userMarriage.photo,
            nvOnline = userMarriage.online
        )
    }
    fun addLanguage(userLP: UserLP){
        addLearnLanguage(userLP.learnLanguage)
        addMotherTongue(userLP.motherTongue)
        allUser(
            nvGender = userLP.gender,
            nvBirthday = userLP.birthday,
            nvAccountType = LANGUAGE_PRACTICE,
            nvUid = userLP.id,
            nvDisplayName = userLP.displayName,
            nvBio = userLP.description,
            nvUsername = userLP.username,
            nvProfileImageUrl = userLP.photo,
            nvOnline = userLP.online
        )
    }

    fun addHotel(hotel: Hotel){
        allBusiness(
            nvAccountType = HOTEL,
            nvUid = hotel.id,
            nvDisplayName = hotel.displayName,
            nvBio = hotel.description,
            nvUsername = hotel.username,
            nvProfileImageUrl = hotel.photo,
            nvAddress = hotel.address,
            nvOnline = hotel.online
        )
    }
    fun addRestaurant(restaurant: Restaurant){
        allBusiness(
            nvAccountType = RESTAURANT,
            nvUid = restaurant.id,
            nvDisplayName = restaurant.displayName,
            nvBio = restaurant.description,
            nvUsername = restaurant.username,
            nvProfileImageUrl = restaurant.photo,
            nvAddress = restaurant.address,
            nvOnline = restaurant.online
        )
    }
    fun addCafe(cafe: Cafe){
        allBusiness(
            nvAccountType = CAFE,
            nvUid = cafe.id,
            nvDisplayName = cafe.displayName,
            nvBio = cafe.description,
            nvUsername = cafe.username,
            nvProfileImageUrl = cafe.photo,
            nvAddress = cafe.address,
            nvOnline = cafe.online
        )
    }

    private fun allUser(
        nvGender: String,
        nvBirthday: String,
        nvAccountType: String,
        nvUid: String,
        nvDisplayName: String,
        nvUsername: String,
        nvProfileImageUrl: String,
        nvBio: String,
        nvOnline: Boolean
    ){
        addGender(nvGender)
        addBirthday(nvBirthday)
        allAccount(
            nvAccountType,
            nvUid,
            nvDisplayName,
            nvUsername,
            nvProfileImageUrl,
            nvBio,
            nvOnline
        )
    }
    private fun allBusiness(
        nvAccountType: String,
        nvUid: String,
        nvDisplayName: String,
        nvUsername: String,
        nvProfileImageUrl: String,
        nvBio: String,
        nvAddress: String,
        nvOnline: Boolean
    ){
        addAddress(nvAddress)
        allAccount(
            nvAccountType,
            nvUid,
            nvDisplayName,
            nvUsername,
            nvProfileImageUrl,
            nvBio,
            nvOnline
        )
    }

    private fun allAccount(
        nvAccountType: String,
        nvUid: String,
        nvDisplayName: String,
        nvUsername: String,
        nvProfileImageUrl: String,
        nvBio: String,
        nvOnline: Boolean,
    ){
        addAccountType(nvAccountType)
        addUid(nvUid)
        addDisplayName(nvDisplayName)
        addUsername(nvUsername)
        addProfileImageUrl(nvProfileImageUrl)
        addBio(nvBio)
        addOnline(nvOnline)
    }


    private fun addAccountType(newValue: String) { _accountType.value = newValue }
    private fun addOnline(newValue: Boolean) { _online.value = newValue }
    private fun addUid(newValue: String) { _uid.value = newValue }
    private fun addDisplayName(newValue: String) { _displayName.value = newValue }
    private fun addUsername(newValue: String) { _username.value = newValue }
    private fun addProfileImageUrl(newValue: String) { _profileImageUrl.value = newValue }
    private fun addBio(newValue: String) { _bio.value = newValue }
    private fun addGender(newValue: String) { _gender.value = newValue }
    private fun addBirthday(newValue: String) { _birthday.value = newValue }
    private fun addLearnLanguage(newValue: String) { _learnLanguage.value = newValue }
    private fun addMotherTongue(newValue: String) { _motherTongue.value = newValue }
    private fun addAddress(newValue: String) { _address.value = newValue }
}
