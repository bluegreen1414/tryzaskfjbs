package com.kapirti.social_chat_food_video.common.ext.search

import com.kapirti.social_chat_food_video.model.Block
import com.kapirti.social_chat_food_video.model.CountryFlag
import com.kapirti.social_chat_food_video.ui.presentation.profile.edit.nickname.Nickname
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object SearchRepo {
    //fun getCategories(): List<SearchCategoryCollection> = searchCategoryCollections

    suspend fun search(query: String, items: List<Nickname>): List<Nickname> = withContext(Dispatchers.Default) {
        delay(200L)
        items.filter { it.nickname.contains(query, ignoreCase = true) }
    }

    suspend fun searchBlockUser(query: String, items: List<Block>): List<Block> = withContext(Dispatchers.Default) {
        delay(200L)
        items.filter { it.nickname.contains(query, ignoreCase = true) }
    }

    suspend fun searchCountry(query: String, items: List<CountryFlag>): List<CountryFlag> = withContext(Dispatchers.Default) {
        delay(200L)
        items.filter {
            //stringResource(it.country).contains(query, ignoreCase = true)}
            it.country.contains(query, ignoreCase = true) }
    }
    suspend fun searchLanguage(query: String, items: List<String>): List<String> = withContext(Dispatchers.Default) {
        delay(200L)
        items.filter { it.contains(query, ignoreCase = true) }
    }
}


enum class SearchDisplay {
    Categories, Suggestions, Results, NoResults
}
